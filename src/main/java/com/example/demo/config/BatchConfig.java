package com.example.demo.config;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.batch.ExcelStudent;
import com.example.demo.batch.ExcelStudentReader;
import com.example.demo.dto.StudentBatchResult;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Configuration 
public class BatchConfig {
    @Bean
    public Job firstJob(
        JobRepository jobRepository,
         Step firstStep,
        Step secondStep,
        Step thirdStep,
        JobExecutionDecider studentDecider) {
        return new JobBuilder("firstJob", jobRepository)
                // .start(firstStep)
                .start(secondStep)
                // .next(secondStep)
                .next(studentDecider)
                .on("PROCESS")
                // .on("CUSTOM_SUCCESS")
                .to(thirdStep)
                // .end()
                // .from(secondStep)
                // .on("*")
                // .fail()
                // .stop()
                // .end()
                .from(studentDecider)
                .on("SKIP")
                .end()
                .end()
                .listener(new BatchJobListener())
                .build();
    }

    // stop fighting maven job id issue for second job running 
    // deleting second job status/row from db and running it whenver needed
    // DELETE FROM batch_step_execution_context;
    // DELETE FROM batch_step_execution;
    // DELETE FROM batch_job_execution_context;
    // DELETE FROM batch_job_execution_params;
    // DELETE FROM batch_job_execution;
    // DELETE FROM batch_job_instance
    // WHERE job_name = 'excelImportJob';
    // mvn spring-boot:run "-Dspring-boot.run.arguments=--spring.batch.job.name=excelImportJob --spring.batch.job.parameters=run.id=1"

    @Bean
    public Job excelImportJob(
        JobRepository jobRepository,
        Step excelImportStep
    ){
        return new JobBuilder("excelImportJob",jobRepository)
                    .start(excelImportStep)
                    .build();
    }

    @Bean
    public Step excelImportStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
        ExcelStudentReader excelStudentReader,
        ItemProcessor<ExcelStudent,StudentBatchResult> excelStudentProcessor,
        JdbcBatchItemWriter<StudentBatchResult> jdbcBatchItemWriter
    ){
        return new StepBuilder("excelImporStep",jobRepository)
                .<ExcelStudent,StudentBatchResult>chunk(2,transactionManager)
                .reader(excelStudentReader)
                .processor(excelStudentProcessor)
                // .writer(items ->{
                    // for(StudentBatchResult result: items){
                        // System.out.println(
                            // ">>> Excel item: "
                            // + result.studentId()
                            // + " - "
                            // + result.studentName()
                            // + " - "
                            // + result.processedName()
                        // );
                    // }
                // })
                .writer(jdbcBatchItemWriter)
                .build();
    }

    @Bean 
    public Step firstStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
        // ListItemReader<Integer> reader,
        RepositoryItemReader<Student> studentReader,
        // ItemProcessor<Integer,Integer> processor,
        // ItemProcessor<Student,Student> studentProcessor,
        ItemProcessor<Student,StudentBatchResult> studentProcessor,
        // ItemWriter<Integer> writer,
        // ItemWriter<Student> studentWriter
        JdbcBatchItemWriter<StudentBatchResult> jdbcBatchItemWriter
    ) {

        return new StepBuilder("firstStep", jobRepository)
                // .<Integer,Integer>chunk(2, transactionManager)
                // .<Student,Student>chunk(2, transactionManager)
                .<Student,StudentBatchResult>chunk(2, transactionManager)
                .reader(studentReader)
                .processor(studentProcessor)
                // .writer(studentWriter)
                .writer(jdbcBatchItemWriter)
                // .faultTolerant()
                // .skip(RuntimeException.class)
                // .skipLimit(1)
                .build();
    }

    @Bean
    public Step secondStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ){
        return new StepBuilder("secondStep", jobRepository)
            .tasklet((contribution,chunkContext)->{
                System.out.println(">>> Second step is running!");
                // contribution.setExitStatus(
                //     new ExitStatus("CUSTOM_OTHER")
                // );
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .listener(new BatchStepListener())
            .build();
    }

    @Bean
    public Step thirdStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {

        return new StepBuilder("thirdStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println(">>> Third step is running!");

                    return RepeatStatus.FINISHED;

                }, transactionManager)
                .build();
    }


    @Bean
    public JobExecutionDecider studentDecider() {
        return (jobExecution, stepExecution) -> {

            System.out.println(">>> Decider is running!");
            boolean shouldProcess = false;
            if(shouldProcess){
            return new FlowExecutionStatus("PROCESS");
            }
            return new FlowExecutionStatus("SKIP");
        };
    }

    // @Bean 
    // public Step firstStep(
    //         JobRepository jobRepository,
    //         PlatformTransactionManager transactionManager) {

    //     return new StepBuilder("firstStep", jobRepository)
    //             .tasklet((contribution, chunkContext) -> {
    //                 System.out.println(">>> First Spring Batch step is running!");
    //                 return RepeatStatus.FINISHED;
    //             }, transactionManager)
    //             .build();
    // }

    // @Bean
    // public ListItemReader<Integer> reader(){
    //     return new ListItemReader<>(
    //         List.of(1,2,3,4,5)
    //     );
    // }

    @Bean
    public RepositoryItemReader<Student> studentReader(
            StudentRepository studentRepository) {

        return new RepositoryItemReaderBuilder<Student>()
                .name("studentReader")
                .repository(studentRepository)
                .methodName("findAll")
                .pageSize(2)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<ExcelStudent, StudentBatchResult> excelStudentProcessor() {

        return student -> new StudentBatchResult(
                student.getId(),
                student.getName(),
                student.getName().toUpperCase()
        );
    }

    @Bean
    public ItemProcessor<Student,StudentBatchResult> studentProcessor(){
        return student ->{
            System.out.println(">>> Processing student: "+student.getName());
            // if("Bob".equalsIgnoreCase(student.getName())){
            //     throw new RuntimeException("Simulated processing error");
            // }
            return new StudentBatchResult(
            student.getId(),
            student.getName(),
            student.getName().toUpperCase()
        );
    };
    }
    // @Bean
    // public ItemProcessor<Student,Student> studentProcessor(){
    //     return student ->{
    //         System.out.println(">>> Processing student: "+student.getName());
    //         return student;
    //     };
    // }
    // @Bean
    // public ItemProcessor<Integer,Integer> processor(){
    //     return item -> item * 10;
    // }

    @Bean
    public ItemWriter<Student> studentWriter(){
        return chunk -> {
            for(Student student: chunk){
                System.out.println(">>> Writing item: "+
                    student.getId() 
                    + " - "
                    + student.getName()
                );
            }
        };
    }
    // @Bean
    // public ItemWriter<Integer> writer(){
    //     return chunk -> {
    //         for(Integer item: chunk){
    //             System.out.println(">>> Writing item: "+item);
    //         }
    //     };
    // }

    @Bean
    public JdbcBatchItemWriter<StudentBatchResult> jdbcBatchItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<StudentBatchResult>()
                .sql("""
                        INSERT INTO student_batch_results
                        (student_id, student_name, processed_name)
                        VALUES (?,?,?)
                        """)
                .dataSource(dataSource)
                // .beanMapped()
                .itemPreparedStatementSetter((result, ps) -> {
                    ps.setInt(1, result.studentId());
                    ps.setString(2, result.studentName());
                    ps.setString(3, result.processedName());
                })
                .build();
    }

    @Bean 
    public ExcelStudentReader excelStudentReader() throws Exception{
        return new ExcelStudentReader();
    }

}
