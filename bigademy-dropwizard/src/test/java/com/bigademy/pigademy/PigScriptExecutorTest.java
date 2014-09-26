package com.bigademy.pigademy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.bigademy.entities.InputDataset;
import com.bigademy.entities.OutputDataset;
import com.bigademy.utils.Response;

@Ignore
public class PigScriptExecutorTest extends PigScriptExecutor {

    @Test
    public void testDummyData() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("123,12,13,14\n112,5,23,10");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(12)\n(5)");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage(',') AS (studentId:int, mark1:int, mark2:int, mark3:int); Result = FOREACH A GENERATE mark1;";

        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testForeachGenerateExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("S1,90,87,76\nS2,70,68,52\nS3,48,92,70\nS4,92,88,98\n,72,62,58");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(S1,253)\n(S2,190)\n(S3,210)\n(S4,278)\n(,192)");
        output.add(out);


        String pigScript = "inputData = LOAD '$input' USING PigStorage(',') AS (studentId:chararray, mark1:int, mark2:int, mark3:int); Result = FOREACH inputData GENERATE studentId, (mark1 + mark2 + mark3) as total;";

        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testFilterExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("S1,90,87,76\nS2,70,68,52\nS3,48,92,70\nS4,92,88,98\n,72,62,58");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(S2,63)\n(S3,70)");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage(',') AS (studentId:chararray, mark1:int, mark2:int, mark3:int); Average = FOREACH A GENERATE studentId, (mark1 + mark2 + mark3)/3 as average; Result = FILTER Average BY (average >= 50) AND (average <= 70) AND (studentId IS NOT null);";

        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testDereferenceTupleExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");

        in.setDataset("S1;Fall-2012;(90,80,70)\nS2;Fall-2012;(75,80,70)\nS1;Spring-2013;(80,92,60)\nS2;Fall-2013;(69,58,57)");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(S1,Fall-2012,240)\n(S2,Fall-2012,225)\n(S1,Spring-2013,232)\n(S2,Fall-2013,184)");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage(';') AS (studentId:chararray, semester:chararray, mark:tuple(mark1:int, mark2:int, mark3:int)); Result = FOREACH A GENERATE studentId,semester,(mark.mark1 + mark.mark2 + mark.mark3) as total;";

        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testDereferenceBagExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("1;{(1,55),(2,90),(3,95)}\n2;{(1,60),(2,70),(3,80)}\n3;{(1,40),(2,50),(3,60)}");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(1,80.0)\n" +
                "(2,70.0)\n" +
                "(3,50.0)");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage(';') AS (studentId: chararray, courseAndMarkInfo:bag{tuple(courseId: chararray, mark: int)});\n" +
                "Result = FOREACH A GENERATE  studentId , AVG(courseAndMarkInfo.mark);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testDereferenceBagTupleExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("S1,Fall-2012,123\nS2,Spring-2013,123\nS1,Spring-2011,213\nS2,Fall-2012,123\nS3,Spring-2011,213\nS3,Fall-2012,123");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(Spring-2011,213,{(S1),(S3)})\n(Fall-2012,123,{(S1),(S2),(S3)})\n(Spring-2013,123,{(S2)})");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage(',') AS (studentId:chararray, semester:chararray, totalMark:int); sameMarks = GROUP A BY (totalMark,semester); DUMP sameMarks; Result = FOREACH sameMarks GENERATE group.semester, group.totalMark, A.studentId;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testFlattenTupleExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("S1;Fall-2012;(90,80,70)\nS2;Fall-2012;(75,80,70)\nS1;Spring-2013;(80,92,60)\nS2;Fall-2013;(69,58,57)");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(S1,Fall-2012,90,80,70)\n(S2,Fall-2012,75,80,70)\n(S1,Spring-2013,80,92,60)\n(S2,Fall-2013,69,58,57)");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage(';') AS (studentId:chararray, semester:chararray,mark:tuple(mark1:int, mark2:int, mark3:int)); Result = FOREACH A GENERATE studentId, semester, FLATTEN(mark);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testFlattenBagExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("1;{(1,55),(2,90),(3,95)}\n" +
                "2;{(1,60),(2,70),(3,80)}\n" +
                "3;{(1,40),(2,50),(3,60)}\n");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(1,1,55)\n" +
                "(1,2,90)\n" +
                "(1,3,95)\n" +
                "(2,1,60)\n" +
                "(2,2,70)\n" +
                "(2,3,80)\n" +
                "(3,1,40)\n" +
                "(3,2,50)\n" +
                "(3,3,60)");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage(';') AS (studentId: chararray, courseAndMarkInfo:bag{tuple(courseId: chararray, mark: int)});DUMP A;Result = FOREACH A GENERATE $0, FLATTEN ($1);DUMP Result;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testInnerJoinExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("courses");

        InputDataset in2 = new InputDataset();
        in2.setAlias("marks");

        in1.setDataset("CSC 107,Programming Fundamentals\nMAT 186, Linear Algebra\nCSC 243,Introduction to Databases\nECE 212,Circuit Analysis\nMAT 196,Calculus I\nAPS 111,Engineering Communication");
        in2.setDataset("S1,CSC 243, 87\nS2,APS 111, 82\nS1,MAT 186, 90\nS2,ECE 212, 72\nS1,APS 252, 77\nS3,PSY 101, 71");
        input.add(in1);
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(APS 111,Engineering Communication,S2,APS 111,82)\n(CSC 243,Introduction to Databases,S1,CSC 243,87)\n(ECE 212,Circuit Analysis,S2,ECE 212,72)\n(MAT 186, Linear Algebra,S1,MAT 186,90)");
        output.add(out);

        String pigScript = "C = LOAD '$courses' USING PigStorage(',') AS (courseId:chararray, courseName:chararray); M = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); Result = JOIN C BY courseId, M BY courseId;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testOuterJoinLeftExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("courses");

        InputDataset in2 = new InputDataset();
        in2.setAlias("marks");

        in1.setDataset("CSC 107,Programming Fundamentals\nMAT 186, Linear Algebra\nCSC 243,Introduction to Databases\nECE 212,Circuit Analysis\nMAT 196,Calculus I\nAPS 111,Engineering Communication");
        in2.setDataset("S1,CSC 243, 87\nS2,APS 111, 82\nS1,MAT 186, 90\nS2,ECE 212, 72\nS1,APS 252, 77\nS3,PSY 101, 71");
        input.add(in1);
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(APS 111,Engineering Communication,S2,APS 111,82)\n(CSC 107,Programming Fundamentals,,,)\n(CSC 243,Introduction to Databases,S1,CSC 243,87)\n(ECE 212,Circuit Analysis,S2,ECE 212,72)\n(MAT 186, Linear Algebra,S1,MAT 186,90)\n(MAT 196,Calculus I,,,)");
        output.add(out);

        String pigScript = "C = LOAD '$courses' USING PigStorage(',') AS (courseId:chararray, courseName:chararray); M = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); Result = JOIN C BY courseId LEFT OUTER, M BY courseId;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testOuterJoinRightExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("courses");

        InputDataset in2 = new InputDataset();
        in2.setAlias("marks");

        in1.setDataset("CSC 107,Programming Fundamentals\nMAT 186, Linear Algebra\nCSC 243,Introduction to Databases\nECE 212,Circuit Analysis\nMAT 196,Calculus I\nAPS 111,Engineering Communication");
        in2.setDataset("S1,CSC 243, 87\nS2,APS 111, 82\nS1,MAT 186, 90\nS2,ECE 212, 72\nS1,APS 252, 77\nS3,PSY 101, 71");
        input.add(in1);
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(APS 111,Engineering Communication,S2,APS 111,82)\n(,,S1,APS 252,77)\n(CSC 243,Introduction to Databases,S1,CSC 243,87)\n(ECE 212,Circuit Analysis,S2,ECE 212,72)\n(MAT 186, Linear Algebra,S1,MAT 186,90)\n(,,S3,PSY 101,71)");
        output.add(out);

        String pigScript = "C = LOAD '$courses' USING PigStorage(',') AS (courseId:chararray, courseName:chararray); M = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); Result = JOIN C BY courseId RIGHT OUTER, M BY courseId;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testOuterJoinFullExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("courses");

        InputDataset in2 = new InputDataset();
        in2.setAlias("marks");

        in1.setDataset("CSC 107,Programming Fundamentals\nMAT 186, Linear Algebra\nCSC 243,Introduction to Databases\nECE 212,Circuit Analysis\nMAT 196,Calculus I\nAPS 111,Engineering Communication");
        in2.setDataset("S1,CSC 243, 87\nS2,APS 111, 82\nS1,MAT 186, 90\nS2,ECE 212, 72\nS1,APS 252, 77\nS3,PSY 101, 71");
        input.add(in1);
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(APS 111,Engineering Communication,S2,APS 111,82)\n(,,S1,APS 252,77)\n(CSC 107,Programming Fundamentals,,,)\n(CSC 243,Introduction to Databases,S1,CSC 243,87)\n(ECE 212,Circuit Analysis,S2,ECE 212,72)\n(MAT 186, Linear Algebra,S1,MAT 186,90)\n(MAT 196,Calculus I,,,)\n(,,S3,PSY 101,71)");
        output.add(out);

        String pigScript = "C = LOAD '$courses' USING PigStorage(',') AS (courseId:chararray, courseName:chararray); M = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); Result = JOIN C BY courseId FULL OUTER, M BY courseId;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupBasicCaseExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("marks");
        in.setDataset("S1,CSC 103, 92\nS2,ECE 445, 67\nS1,HPS 162, 78\nS3,PSY 101, 70\nS2,CSC 103, 92\nS1,APS 105, 72\nS2,PSY 101, 80\nS3,HPS 162, 78\nS3,CSC 103, 68");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(APS 105,{(S1,APS 105,72)})\n(CSC 103,{(S1,CSC 103,92),(S2,CSC 103,92),(S3,CSC 103,68)})\n(ECE 445,{(S2,ECE 445,67)})\n(HPS 162,{(S1,HPS 162,78),(S3,HPS 162,78)})\n(PSY 101,{(S3,PSY 101,70),(S2,PSY 101,80)})");
        output.add(out);

        String pigScript = "A = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); Result = GROUP A BY courseId;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupForEachExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("marks");
        in.setDataset("S1,CSC 103, 92\nS2,ECE 445, 67\nS1,HPS 162, 78\nS3,PSY 101, 70\nS2,CSC 103, 92\nS1,APS 105, 72\nS2,PSY 101, 80\nS3,HPS 162, 78\nS3,CSC 103, 68");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(APS 105,72.0)\n(CSC 103,84.0)\n(ECE 445,67.0)\n(HPS 162,78.0)\n(PSY 101,75.0)");
        output.add(out);

        String pigScript = "A = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); allMarks = GROUP A BY courseId; Result = FOREACH allMarks GENERATE group, AVG(A.mark);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupExpressionExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("marks");

        in.setDataset("S1, 72, 90, 67\nS2, 82, 71,76\nS3, 96, 54, 79\nS4, 71, 62, 90\nS5, 57, 85, 81\nS6, 94, 68, 89");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(74,{(S4,71,62,90),(S5,57,85,81)})\n(76,{(S1,72,90,67),(S2,82,71,76),(S3,96,54,79)})\n(83,{(S6,94,68,89)})");
        output.add(out);

        String pigScript = "A = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, mark1:int, mark2:int, mark3:int); Result = GROUP A BY ((mark1+mark2 +mark3)/3);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupMutipleFieldsExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("marks");
        in.setDataset("S1,CSC 103, 92\nS2,ECE 445, 67\nS1,HPS 162, 78\nS3,PSY 101, 70\nS2,CSC 103, 92\nS1,APS 105, 72\nS2,PSY 101, 80\nS3,HPS 162, 78\nS3,CSC 103, 68");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("((APS 105,72),{(S1,APS 105,72)})\n((CSC 103,68),{(S3,CSC 103,68)})\n((CSC 103,92),{(S1,CSC 103,92),(S2,CSC 103,92)})\n((ECE 445,67),{(S2,ECE 445,67)})\n((HPS 162,78),{(S1,HPS 162,78),(S3,HPS 162,78)})\n((PSY 101,70),{(S3,PSY 101,70)})\n((PSY 101,80),{(S2,PSY 101,80)})");
        output.add(out);

        String pigScript = "A = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); Result = GROUP A BY (courseId, mark);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupForEachBlockExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("marks");
        in.setDataset("S1,CSC 103, 92\nS2,ECE 445, 67\nS1,HPS 162, 78\nS3,PSY 101, 70\nS2,CSC 103, 92\nS1,APS 105, 72\nS2,PSY 101, 80\nS3,HPS 162, 78\nS3,CSC 103, 68");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(APS 105,{(S1)})\n(CSC 103,{(S1),(S2)})\n(ECE 445,{})\n(HPS 162,{(S1),(S3)})\n(PSY 101,{(S2)})");
        output.add(out);

        String pigScript = "A = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); allMarks = GROUP A BY courseId; Result = FOREACH allMarks { aboveAverage = FILTER A BY (mark > 70); GENERATE group, aboveAverage.studentId;};";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testCoGroupExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("studentMarkInfo");
        in1.setDataset("1,1,1,60\n" +
                "1,1,2,80\n" +
                "1,1,3,70\n" +
                "2,2,1,80\n" +
                "2,2,2,90\n" +
                "2,2,3,70");
        input.add(in1);

        InputDataset in2 = new InputDataset();
        in2.setAlias("courseInfo");
        in2.setDataset("1,1,Scott\n" +
                "2,2,Mark");
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(Scott,70.0)\n" +
                "(Mark,80.0)");
        output.add(out);

//        String pigScript = "A = LOAD '$students' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int, profId:chararray);B = LOAD '$profs' USING PigStorage(',') AS (profId:chararray, courseId:chararray, avgMark:int); coGroupedData = COGROUP B BY (courseId, profId), A BY (courseId, profId); Result = FOREACH coGroupedData {profAvgMark = LIMIT B 1; studentsAboveAverage = FILTER A BY (A.mark >= profAvgMark.avgMark); GENERATE profAvgMark.courseId, profAvgMark.profId, studentsAboveAverage.studentId;};";
        String pigScript = "studentMarkInfo = LOAD '$studentMarkInfo' USING PigStorage(',') AS (courseId: int, semId: int, studentId: int, marks: int);\n" +
                "courseInfo = LOAD '$courseInfo' USING PigStorage(',') AS (courseId: int, semId: int, profId: chararray);\n" +
                "coGroupedData = COGROUP studentMarkInfo BY (courseId, semId), courseInfo BY (courseId, semId);\n" +
                "Result = FOREACH coGroupedData {\n" +
                "\t\t\tavgMark = AVG(studentMarkInfo.marks);\n" +
                "\t\t\tGENERATE \n" +
                "\t\t\t\tFLATTEN(courseInfo.profId), avgMark;\n" +
                "};";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testDistinctExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("marks");
        in.setDataset("S1,CSC 103, 92\nS2,ECE 445, 67\nS1,HPS 162, 78\nS3,PSY 101, 70\nS2,CSC 103, 92\nS1,APS 105, 72\nS2,PSY 101, 80\nS3,HPS 162, 78\nS3,CSC 103, 68\nS2,ECE 445, 67\nS1,HPS 162, 78\nS3,PSY 101, 70");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(S1,APS 105,72)\n(S1,CSC 103,92)\n(S1,HPS 162,78)\n(S2,CSC 103,92)\n(S2,ECE 445,67)\n(S2,PSY 101,80)\n(S3,CSC 103,68)\n(S3,HPS 162,78)\n(S3,PSY 101,70)");
        output.add(out);

        String pigScript = "A = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); Result = DISTINCT A;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testOrderByExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("marks");
        in.setDataset("S1,CSC 103, 92\nS2,ECE 445, 67\nS1,HPS 162, 78\nS3,PSY 101, 70\nS2,CSC 103, 92\nS1,APS 105, 72\nS2,PSY 101, 80\nS3,HPS 162, 78\nS3,CSC 103, 68");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(S1,APS 105,72)\n(S1,CSC 103,92)\n(S1,HPS 162,78)\n(S2,CSC 103,92)\n(S2,ECE 445,67)\n(S2,PSY 101,80)\n(S3,CSC 103,68)\n(S3,HPS 162,78)\n(S3,PSY 101,70)");
        output.add(out);

        String pigScript = "A = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); Result = ORDER A BY studentId ASC, courseId ASC;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testLimitExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("marks");
        in.setDataset("S1,CSC 103, 92\nS2,ECE 445, 67\nS1,HPS 162, 78\nS3,PSY 101, 70\nS2,CSC 103, 92\nS1,APS 105, 72\nS2,PSY 101, 80\nS3,HPS 162, 78\nS3,CSC 103, 68");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(S1,CSC 103,92)\n(S2,CSC 103,92)\n(S2,PSY 101,80)");
        output.add(out);

        String pigScript = "A = LOAD '$marks' USING PigStorage(',') AS (studentId:chararray, courseId:chararray, mark:int); orderedMarks = ORDER A BY mark DESC; Result = LIMIT orderedMarks 3;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testUnionBaseCaseExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("students1");
        in1.setDataset("S1, 92\nS2, 67\nS1, 78\nS3, 70");
        input.add(in1);

        InputDataset in2 = new InputDataset();
        in2.setAlias("students2");
        in2.setDataset("S2, 72\nS2, 80\nS3, 78\nS3, 68");
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(S1,78)\n(S1,92)\n(S2,67)\n(S2,72)\n(S2,80)\n(S3,68)\n(S3,70)\n(S3,78)");
        output.add(out);

        String pigScript = "A = LOAD '$students1' USING PigStorage(',') AS (studentId:chararray, mark:int); B = LOAD '$students2' USING PigStorage(',') AS (studentId:chararray, mark:int);allStudents = UNION A, B; Result = ORDER allStudents BY studentId ASC, mark ASC;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testUnionOnSchemaExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("students1");
        in1.setDataset("S1, 92\nS2, 67\nS1, 78\nS3, 70");
        input.add(in1);

        InputDataset in2 = new InputDataset();
        in2.setAlias("students2");
        in2.setDataset("72,S2\n80,S2\n78,S3\n68,S3");
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("Result");
        out.setDataset("(S1,92)\n(S2,67)\n(S1,78)\n(S3,70)\n(S2,72)\n(S2,80)\n(S3,78)\n(S3,68)");
        output.add(out);

        String pigScript = "A = LOAD '$students1' USING PigStorage(',') AS (studentId:chararray, mark:int); B = LOAD '$students2' USING PigStorage(',') AS (mark:int, studentId:chararray);Result = UNION ONSCHEMA A, B;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testSplitExercise() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("students");
        in.setDataset("S1, 32\nS2, 70\nS1, 49\nS3, 60\nS2, 83\nS2, 47\nS3, 78\nS3, 50");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out1 = new OutputDataset();
        out1.setAlias("belowAvg");
        out1.setDataset("(S1,32)\n(S1,49)\n(S2,47)\n(S3,50)");
        output.add(out1);

        OutputDataset out2 = new OutputDataset();
        out2.setAlias("Avg");
        out2.setDataset("(S2,70)\n(S3,60)");
        output.add(out2);

        OutputDataset out3 = new OutputDataset();
        out3.setAlias("aboveAvg");
        out3.setDataset("(S2,83)\n(S3,78)");
        output.add(out3);

        String pigScript = "A = LOAD '$students' USING PigStorage(',') AS (studentId:chararray, mark:int); SPLIT A INTO belowAvg IF mark<=50, Avg IF (mark>50 AND mark<=70), aboveAvg IF (mark>70);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    private void assertSuccessResponse(PigScriptExecutor exec, Set<InputDataset> input,
                                       Set<OutputDataset> output, String pigScript) {
        Response expectedResponse = new Response();
        expectedResponse.setSuccess(true);

        Response response = exec.executeAndAssertOutput(input, output, pigScript);

        System.out.println("///////////////");
        System.out.println(response.getErrorMessage());
        System.out.println("///////////////");

        assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testForeachGenerateExample_Projection() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");
        in.setDataset("1,2,3\n4,5,6");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("B");
        out.setDataset("(1,2)\n(4,5)");
        output.add(out);

        String pigScript = "A = LOAD '$data' USING PigStorage(',') AS (a1:int, a2:int, a3:int); B = FOREACH A GENERATE a1, a2;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testForeachGenerateExample_DefineSchemaUseOperators() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");
        in.setDataset("1,2,3\n4,5,6");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("C");
        out.setDataset("(3)\n(9)");
        output.add(out);

        String pigScript = "A = LOAD '$data' USING PigStorage(',') AS (a1:int, a2:int, a3:int); C = FOREACH A GENERATE a1+a2 AS sum:int;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testFilterExample_Single() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");
        in.setDataset("1,2,3\n4,7,0\n7,8,9");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("B");
        out.setDataset("(4,7,0)\n(7,8,9)");
        output.add(out);

        String pigScript = "A = LOAD '$data' USING PigStorage(',') AS (a1:int, a2:int, a3:int); B = FILTER A BY (a1 > 2);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testFilterExample_Multiple() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");
        in.setDataset("1,2,3\n4,7,0\n,5,9");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("B");
        out.setDataset("(4,7,0)\n(,5,9)");
        output.add(out);

        String pigScript = "A = LOAD '$data' USING PigStorage(',') AS (a1:int, a2:int, a3:int); B = FILTER A BY (a1 + a3 == 4) AND (NOT( a3 > a1)) OR (a1 IS null);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testDereferenceTupleExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("1;(1,2,3)\n2;(4,5,6)\n3;(7,8,9)\n4;(1,4,7)\n5;(2,5,8)");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("X");
        out.setDataset("(1,3)\n(4,6)\n(7,9)\n(1,7)\n(2,8)");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage(';') AS (f1:int, f2:tuple(t1:int, t2:int, t3:int));X = FOREACH A GENERATE f2.t1,f2.t3;";

        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testDereferenceTupleBagExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("ratings");
        in.setDataset("critic1,The Shawshank Redemption, 9.0\n" +
                "critic3,The Dark Knight, 8.8\n" +
                "critic3,The Shawshank Redemption, 9.5\n" +
                "critic1,Schindler's List, 9.0\n" +
                "critic1,The Dark Knight, 8.8\n" +
                "critic2,The Shawshank Redemption, 9.0\n" +
                "critic2,The Dark Knight, 8.8");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("movies");
        out.setDataset("(The Dark Knight,{(critic3),(critic1),(critic2)})\n" +
                "(Schindler's List,{(critic1)})\n" +
                "(The Shawshank Redemption,{(critic1),(critic3),(critic2)})");
        output.add(out);

        String pigScript = "ratings = LOAD '$ratings' USING PigStorage(',') AS (criticId:chararray, movie:chararray, rating:long); sameRatings = GROUP ratings BY (movie,rating); movies = FOREACH sameRatings GENERATE group.movie, ratings.criticId;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testFlattenBagExample1() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("{(b,c),(d,e)}\n" +
                "{(f,g),(h,i)}\n" +
                "{(j,k),(l,m)}");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("B");
        out.setDataset("(b,c)\n" +
                "(d,e)\n" +
                "(f,g)\n" +
                "(h,i)\n" +
                "(j,k)\n" +
                "(l,m)");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage() AS (a:bag{b:tuple(f1:chararray, f2:chararray)});DUMP A; B = FOREACH A GENERATE FLATTEN($0);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testFlattenBagExample2() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("input");
        in.setDataset("a;{(b,c), (d,e)}\n" +
                "f;{(g,h), (i,j)}\n" +
                "k;{(l,m), (n,o)}");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("B");
        out.setDataset("(a,b,c)\n" +
                "(a,d,e)\n" +
                "(f,g,h)\n" +
                "(f,i,j)\n" +
                "(k,l,m)\n" +
                "(k,n,o)");
        output.add(out);

        String pigScript = "A = LOAD '$input' USING PigStorage(';') AS (t: chararray, a:bag{b:tuple(f1:chararray, f2:chararray)});DUMP A; B = FOREACH A GENERATE $0, FLATTEN($1);";
        assertSuccessResponse(exec, input, output, pigScript);
    }


    @Test
    public void testFlattenTupleExample_Position() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");
        in.setDataset("a\t(b,c)\n" +
                "d\t(e,f)\n" +
                "g\t(h,i)");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("B");
        out.setDataset("(a,b,c)\n" +
                "(d,e,f)\n" +
                "(g,h,i)");
        output.add(out);

        String pigScript = "A = LOAD '$data' USING PigStorage('\t') AS (t1:chararray, t2:tuple(f1:chararray, f2:chararray)); B = FOREACH A GENERATE $0, FLATTEN($1);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testFlattenTupleExample_Names() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");
        in.setDataset("a\t(b,c)\n" +
                "d\t(e,f)\n" +
                "g\t(h,i)");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("B");
        out.setDataset("(a,b,c)\n" +
                "(d,e,f)\n" +
                "(g,h,i)");
        output.add(out);

        String pigScript = "A = LOAD '$data' USING PigStorage('\t') AS (t1:chararray, t2:tuple(f1:chararray, f2:chararray)); B = FOREACH A GENERATE t1, FLATTEN(t2);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testInnerJoinExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("directors");

        InputDataset in2 = new InputDataset();
        in2.setAlias("ratings");

        in1.setDataset("Francis Lawrence,The Hunger Games-Catching Fire\n" +
                "Christopher Nolan,The Dark Knight Rises\n" +
                "Francis Lawrence,I am Legend\n" +
                "Christopher Nolan,Inception\n" +
                "Christopher Nolan,The Dark Knight\n" +
                "Pierre Coffin,Despicable Me\n" +
                "Sergio Leone,The Good, the Bad and the Ugly\n" +
                "Sidney Lumet,Angry Men");
        in2.setDataset("The Hunger Games-Catching Fire, 8.0\n" +
                "The Dark Knight Rises, 8.6\n" +
                "I am Legend, 7.2\n" +
                "Inception, 8.8\n" +
                "The Dark Knight, 9.0\n" +
                "Despicable Me, 7.7\n" +
                "The Shawshank Redemption, 9.2\n" +
                "Pulp Fiction, 8.9");
        input.add(in1);
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("movieInfo");
        out.setDataset("(Christopher Nolan,Inception,Inception, 8.8)\n" +
                "(Francis Lawrence,I am Legend,I am Legend, 7.2)\n" +
                "(Pierre Coffin,Despicable Me,Despicable Me, 7.7)\n" +
                "(Christopher Nolan,The Dark Knight,The Dark Knight, 9.0)\n" +
                "(Christopher Nolan,The Dark Knight Rises,The Dark Knight Rises, 8.6)\n" +
                "(Francis Lawrence,The Hunger Games-Catching Fire,The Hunger Games-Catching Fire, 8.0)");
        output.add(out);

        String pigScript = "directors = LOAD '$directors' USING PigStorage(',') AS (director:chararray, movie:chararray); ratings = LOAD '$ratings' USING PigStorage(',') AS (movie:chararray, rating:chararray); movieInfo = JOIN directors BY movie, ratings BY movie;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testOuterJoinLeftExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("directors");

        InputDataset in2 = new InputDataset();
        in2.setAlias("ratings");

        in1.setDataset("Francis Lawrence,The Hunger Games-Catching Fire\n" +
                "Christopher Nolan,The Dark Knight Rises\n" +
                "Francis Lawrence,I am Legend\n" +
                "Christopher Nolan,Inception\n" +
                "Christopher Nolan,The Dark Knight\n" +
                "Pierre Coffin,Despicable Me\n" +
                "Sergio Leone,The Good, the Bad and the Ugly\n" +
                "Sidney Lumet,Angry Men");
        in2.setDataset("The Hunger Games-Catching Fire, 8.0\n" +
                "The Dark Knight Rises, 8.6\n" +
                "I am Legend, 7.2\n" +
                "Inception, 8.8\n" +
                "The Dark Knight, 9.0\n" +
                "Despicable Me, 7.7\n" +
                "The Shawshank Redemption, 9.2\n" +
                "Pulp Fiction, 8.9");
        input.add(in1);
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("movieInfo");
        out.setDataset("(Sergio Leone,The Good,,)\n" +
                "(Sidney Lumet,Angry Men,,)\n" +
                "(Christopher Nolan,Inception,Inception, 8.8)\n" +
                "(Francis Lawrence,I am Legend,I am Legend, 7.2)\n" +
                "(Pierre Coffin,Despicable Me,Despicable Me, 7.7)\n" +
                "(Christopher Nolan,The Dark Knight,The Dark Knight, 9.0)\n" +
                "(Christopher Nolan,The Dark Knight Rises,The Dark Knight Rises, 8.6)\n" +
                "(Francis Lawrence,The Hunger Games-Catching Fire,The Hunger Games-Catching Fire, 8.0)");
        output.add(out);

        String pigScript = "directors = LOAD '$directors' USING PigStorage(',') AS (director:chararray, movie:chararray); ratings = LOAD '$ratings' USING PigStorage(',') AS (movie:chararray, rating:chararray); movieInfo = JOIN directors BY movie LEFT OUTER, ratings BY movie;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testOuterJoinRightExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("directors");

        InputDataset in2 = new InputDataset();
        in2.setAlias("ratings");

        in1.setDataset("Francis Lawrence,The Hunger Games-Catching Fire\n" +
                "Christopher Nolan,The Dark Knight Rises\n" +
                "Francis Lawrence,I am Legend\n" +
                "Christopher Nolan,Inception\n" +
                "Christopher Nolan,The Dark Knight\n" +
                "Pierre Coffin,Despicable Me\n" +
                "Sergio Leone,The Good, the Bad and the Ugly\n" +
                "Sidney Lumet,Angry Men");
        in2.setDataset("The Hunger Games-Catching Fire, 8.0\n" +
                "The Dark Knight Rises, 8.6\n" +
                "I am Legend, 7.2\n" +
                "Inception, 8.8\n" +
                "The Dark Knight, 9.0\n" +
                "Despicable Me, 7.7\n" +
                "The Shawshank Redemption, 9.2\n" +
                "Pulp Fiction, 8.9");
        input.add(in1);
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("movieInfo");
        out.setDataset("(Christopher Nolan,Inception,Inception, 8.8)\n" +
                "(Francis Lawrence,I am Legend,I am Legend, 7.2)\n" +
                "(,,Pulp Fiction, 8.9)\n" +
                "(Pierre Coffin,Despicable Me,Despicable Me, 7.7)\n" +
                "(Christopher Nolan,The Dark Knight,The Dark Knight, 9.0)\n" +
                "(Christopher Nolan,The Dark Knight Rises,The Dark Knight Rises, 8.6)\n" +
                "(,,The Shawshank Redemption, 9.2)\n" +
                "(Francis Lawrence,The Hunger Games-Catching Fire,The Hunger Games-Catching Fire, 8.0)");
        output.add(out);

        String pigScript = "directors = LOAD '$directors' USING PigStorage(',') AS (director:chararray, movie:chararray); ratings = LOAD '$ratings' USING PigStorage(',') AS (movie:chararray, rating:chararray); movieInfo = JOIN directors BY movie RIGHT OUTER, ratings BY movie;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testOuterJoinFullExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("directors");

        InputDataset in2 = new InputDataset();
        in2.setAlias("ratings");

        in1.setDataset("Francis Lawrence,The Hunger Games-Catching Fire\n" +
                "Christopher Nolan,The Dark Knight Rises\n" +
                "Francis Lawrence,I am Legend\n" +
                "Christopher Nolan,Inception\n" +
                "Christopher Nolan,The Dark Knight\n" +
                "Pierre Coffin,Despicable Me\n" +
                "Sergio Leone,The Good, the Bad and the Ugly\n" +
                "Sidney Lumet,Angry Men");
        in2.setDataset("The Hunger Games-Catching Fire, 8.0\n" +
                "The Dark Knight Rises, 8.6\n" +
                "I am Legend, 7.2\n" +
                "Inception, 8.8\n" +
                "The Dark Knight, 9.0\n" +
                "Despicable Me, 7.7\n" +
                "The Shawshank Redemption, 9.2\n" +
                "Pulp Fiction, 8.9");
        input.add(in1);
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("movieInfo");
        out.setDataset("(Sergio Leone,The Good,,)\n" +
                "(Sidney Lumet,Angry Men,,)\n" +
                "(Christopher Nolan,Inception,Inception, 8.8)\n" +
                "(Francis Lawrence,I am Legend,I am Legend, 7.2)\n" +
                "(,,Pulp Fiction, 8.9)\n" +
                "(Pierre Coffin,Despicable Me,Despicable Me, 7.7)\n" +
                "(Christopher Nolan,The Dark Knight,The Dark Knight, 9.0)\n" +
                "(Christopher Nolan,The Dark Knight Rises,The Dark Knight Rises, 8.6)\n" +
                "(,,The Shawshank Redemption, 9.2)\n" +
                "(Francis Lawrence,The Hunger Games-Catching Fire,The Hunger Games-Catching Fire, 8.0)");
        output.add(out);

        String pigScript = "directors = LOAD '$directors' USING PigStorage(',') AS (director:chararray, movie:chararray); ratings = LOAD '$ratings' USING PigStorage(',') AS (movie:chararray, rating:chararray); movieInfo = JOIN directors BY movie FULL OUTER, ratings BY movie;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupBasicCaseExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");
        in.setDataset("John,18\n" +
                "Mary,19\n" +
                "Bill,20\n" +
                "Joe,18");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("B");
        out.setDataset("(18,{(John,18),(Joe,18)})\n" +
                "(19,{(Mary,19)})\n" +
                "(20,{(Bill,20)})");
        output.add(out);

        String pigScript = "A = LOAD '$data' USING PigStorage(',') AS (name:chararray, age:int); B = GROUP A BY age;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupForeachExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");
        in.setDataset("John,18\n" +
                "Mary,19\n" +
                "Bill,20\n" +
                "Joe,18");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("C");
        out.setDataset("(18,2)\n" +
                "(19,1)\n" +
                "(20,1)");
        output.add(out);

        String pigScript = "A = LOAD '$data' USING PigStorage(',') AS (name:chararray, age:int); B = GROUP A BY age; C = FOREACH B GENERATE group, COUNT(A);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupWithExpressionExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("ratings");
        in.setDataset("The Shawshank Redemption, 9.0, 9.5, 9.0\n" +
                "The Godfather, 8.9, 9.2, 9.4\n" +
                "The Dark Knight, 9.0, 8.7, 8.9\n" +
                "Schindler's List, 8.9, 8.9, 8.9\n" +
                "Fight Club, 8.7, 8.8, 8.9");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("ratingGroup");
        out.setDataset("(8,{(The Godfather,8,9,9),(The Dark Knight,9,8,8),(Schindler's List,8,8,8),(Fight Club,8,8,8)})\n" +
                "(9,{(The Shawshank Redemption,9,9,9)})");
        output.add(out);

        String pigScript = "ratings = LOAD '$ratings' USING PigStorage(',') AS (movie:chararray, rating1:long, rating2:long, rating3:long); ratingGroup = GROUP ratings BY ((rating1+rating2+rating3)/3);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupMultipleFieldsExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("ratings");
        in.setDataset("critic1, The Shawshank Redemption, 9.0\n" +
                "critic3, The Dark Knight, 8.8\n" +
                "critic3, The Shawshank Redemption, 9.5\n" +
                "critic1, Schindler's List, 9.0\n" +
                "critic1, The Dark Knight, 8.0\n" +
                "critic2, The Shawshank Redemption, 9.0\n" +
                "critic2, The Dark Knight, 8.8");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("sameRatings");
        out.setDataset("(( The Dark Knight,8),{(critic3, The Dark Knight,8),(critic1, The Dark Knight,8),(critic2, The Dark Knight,8)})\n" +
                "(( Schindler's List,9),{(critic1, Schindler's List,9)})\n" +
                "(( The Shawshank Redemption,9),{(critic1, The Shawshank Redemption,9),(critic3, The Shawshank Redemption,9),(critic2, The Shawshank Redemption,9)})");
        output.add(out);

        String pigScript = "ratings = LOAD '$ratings' USING PigStorage(',') AS (criticId:chararray, movie:chararray, rating:long); sameRatings = GROUP ratings BY (movie,rating);";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testGroupForEachBlockExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("AllPets");

        in.setDataset("Chris,snake\n" +
                "Derek,dog\n" +
                "Anne,rabbit\n" +
                "William,cat\n" +
                "William,dog\n" +
                "Derek,dog");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("ownerWithDogs");
        out.setDataset("(Anne,0)\n" +
                "(Chris,0)\n" +
                "(Derek,1)\n" +
                "(William,1)");
        output.add(out);

        String pigScript = "AllPets= LOAD '$AllPets' USING PigStorage(',') AS (owner:chararray, pet:chararray); petsPerOwner = GROUP AllPets BY owner; ownerWithDogs = FOREACH petsPerOwner { dogOwners = FILTER AllPets BY pet=='dog'; pet = dogOwners.pet; hasDog = DISTINCT pet; GENERATE group, COUNT(hasDog);}";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testCoGroupExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("petOwners");

        in1.setDataset("Alice,turtle\n" +
                "Alice,goldfish\n" +
                "Alice,cat\n" +
                "Bob,dog\n" +
                "Bob,cat");
        input.add(in1);

        InputDataset in2 = new InputDataset();
        in2.setAlias("friends");

        in2.setDataset("Cindy,Alice\n" +
                "Mark,Alice\n" +
                "Paul,Bob\n" +
                "Paul,Jane");
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("petOwnersAndTheirFriends");
        out.setDataset("(Bob,{(Bob,dog),(Bob,cat)},{(Paul,Bob)})\n" +
                "(Jane,{},{(Paul,Jane)})\n" +
                "(Alice,{(Alice,turtle),(Alice,goldfish),(Alice,cat)},{(Cindy,Alice),(Mark,Alice)})");
        output.add(out);

        String pigScript = "petOwners= LOAD '$petOwners' USING PigStorage(',') AS (owner:chararray, pet:chararray); friends = LOAD '$friends' USING PigStorage(',') AS (friend1:chararray, friend2:chararray); petOwnersAndTheirFriends = COGROUP petOwners BY owner, friends BY friend2;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testDistinctExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");

        in.setDataset("8,3,4\n" +
                "1,2,3\n" +
                "4,3,3\n" +
                "4,3,3\n" +
                "1,2,3");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("X");
        out.setDataset("(1,2,3)\n" +
                "(4,3,3)\n" +
                "(8,3,4)");
        output.add(out);

        String pigScript = "A= LOAD '$data' USING PigStorage(',') AS (a1:int, a2:int, a3:int); X = DISTINCT A;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testOrderByExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");

        in.setDataset("1,2,3\n" +
                "4,2,1\n" +
                "8,3,4\n" +
                "4,3,3\n" +
                "7,2,5\n" +
                "8,4,3");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("X");
        out.setDataset("(7,2,5)\n" +
                "(8,3,4)\n" +
                "(1,2,3)\n" +
                "(4,3,3)\n" +
                "(8,4,3)\n" +
                "(4,2,1)");
        output.add(out);

        String pigScript = "A= LOAD '$data' USING PigStorage(',') AS (a1:int, a2:int, a3:int); X = ORDER A BY a3 DESC;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testLimitExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");

        in.setDataset("1,2,3\n" +
                "4,2,1\n" +
                "8,3,4\n" +
                "4,3,3\n" +
                "7,2,5\n" +
                "8,4,3");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("X");
        out.setDataset("(8,3,4)\n" +
                "(8,4,3)\n" +
                "(7,2,5)");
        output.add(out);

        String pigScript = "A= LOAD '$data' USING PigStorage(',') AS (a1:int, a2:int, a3:int); B = ORDER A BY a1 DESC, a2 ASC; X = LIMIT B 3;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    // TODO: Order of output is undefined. MENTION in the example.
    @Test
    public void testUnionBaseCaseExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("data1");

        in1.setDataset("1,2,3\n" +
                "4,2,1");
        input.add(in1);

        InputDataset in2 = new InputDataset();
        in2.setAlias("data2");

        in2.setDataset("2,4\n" +
                "8,9\n" +
                "1,3");
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("X");
        out.setDataset("(2,4)\n" +
                "(8,9)\n" +
                "(1,3)\n" +
                "(1,2,3)\n" +
                "(4,2,1)");
        output.add(out);

        String pigScript = "A= LOAD '$data1' USING PigStorage(',') AS (a1:int, a2:int, a3:int); B= LOAD '$data2' USING PigStorage(',') AS (b1:int, b2:int); X = UNION A, B;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testUnionOnSchemaExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in1 = new InputDataset();
        in1.setAlias("data1");

        in1.setDataset("11,12.0\n" +
                "21,22.0");
        input.add(in1);

        InputDataset in2 = new InputDataset();
        in2.setAlias("data2");

        in2.setDataset("11,a\n" +
                "12,b\n" +
                "13,c");
        input.add(in2);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out = new OutputDataset();
        out.setAlias("U");
        out.setDataset("(11,12.0,)\n" +
                "(21,22.0,)\n" +
                "(11,,a)\n" +
                "(12,,b)\n" +
                "(13,,c)");
        output.add(out);

        String pigScript = "L1 = LOAD '$data1' USING PigStorage(',') AS (a:int, b:float); L2 = LOAD '$data2' USING PigStorage(',') AS (a:long, c:chararray); U = UNION ONSCHEMA L1, L2;";
        assertSuccessResponse(exec, input, output, pigScript);
    }

    @Test
    public void testSplitExample() {
        PigScriptExecutor exec = new PigScriptExecutor();
        Set<InputDataset> input = new HashSet<InputDataset>();
        InputDataset in = new InputDataset();
        in.setAlias("data");
        in.setDataset("1,2,3\n" +
                "4,5,6\n" +
                "7,8,9");
        input.add(in);

        Set<OutputDataset> output = new HashSet<OutputDataset>();
        OutputDataset out1 = new OutputDataset();
        out1.setAlias("X");
        out1.setDataset("(1,2,3)\n" +
                "(4,5,6)");
        output.add(out1);

        OutputDataset out2 = new OutputDataset();
        out2.setAlias("Y");
        out2.setDataset("(4,5,6)");
        output.add(out2);

        OutputDataset out3 = new OutputDataset();
        out3.setAlias("Z");
        out3.setDataset("(1,2,3)\n" +
                "(7,8,9)");
        output.add(out3);

        String pigScript = "A = LOAD '$data' USING PigStorage(',') AS (f1:int,f2:int,f3:int); SPLIT A INTO X IF (f1<7),Y IF (f2==5),Z IF (f3<6 OR f3>6);";
        assertSuccessResponse(exec, input, output, pigScript);
    }
}