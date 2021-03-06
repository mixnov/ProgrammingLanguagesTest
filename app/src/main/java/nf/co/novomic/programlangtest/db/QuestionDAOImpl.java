package nf.co.novomic.programlangtest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;

import nf.co.novomic.programlangtest.classes.Answer;
import nf.co.novomic.programlangtest.classes.Question;

/**
 * Data access methods for Question entities
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class QuestionDAOImpl {

    /**
     * Generate sql query string
     *
     * @param questionFilter - additional join and/or where clause
     * @param postFilter - clause after order statement
     * @return string of query
     */
    static String getQuestionQuery(String questionFilter, String postFilter) {
        return "SELECT tq." + DBHandler.COLUMN_ID +             //0
                ", tq." + DBHandler.COLUMN_QUESTION +           //1
                ", tq." + DBHandler.COLUMN_TEST +               //2
                ", tq." + DBHandler.COLUMN_CATEGORY_FK +        //3
                ", tq." + DBHandler.COLUMN_MULTIPLE +           //4
                ", tq." + DBHandler.COLUMN_EXPLANATION +        //5
                ", ta." + DBHandler.COLUMN_ID + " answer_id " + //6
                ", ta." + DBHandler.COLUMN_ANSWER +             //7
                ", ta." + DBHandler.COLUMN_CORRECT +            //8
                ", ta." + DBHandler.COLUMN_EXPLANATION +        //9
                " FROM " + DBHandler.TABLE_QUESTION + " tq " +
                " LEFT JOIN " + DBHandler.TABLE_ANSWER +
                " ta ON tq." + DBHandler.COLUMN_ID +
                " = ta." + DBHandler.COLUMN_QUESTION_FK
                + questionFilter +
                " ORDER BY tq." + DBHandler.COLUMN_ID +
                ", RANDOM() " +
                postFilter;
//        Log
    }

    static ArrayList<Question> getQuestions(Context context, String whereClause, String postFilter) {
        // open connection to the database
        DBHandler dbHandler = new DBHandler(context);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        // create empty array list for questions
        ArrayList<Question> questions = new ArrayList<>();
        // create custom query to get data from questions and answers
        String selectQuestions = getQuestionQuery(whereClause, postFilter);
        // execute query
        Cursor cursor = db.rawQuery(selectQuestions, null);

        int current_id = -1;
        char letter = 'A';
        Question question = null;
        String qExplain = "";
        // loop through the results
        if (cursor.moveToFirst()) {
            do {

                int qId = cursor.getInt(0);
                if (qId != current_id) {
                    letter = 'A';
                    String qText = cursor.getString(1);
                    int qTestType = cursor.getInt(2);
                    int qCategory = cursor.getInt(3);
                    boolean qMultiple = (cursor.getInt(4) == 1);
                    qExplain = cursor.getString(5);

                    // create question
                    question = new Question(qId, qText, qCategory, new ArrayList<Answer>(),
                            qMultiple, qTestType, qExplain);

                    // add question to the list of questions
                    questions.add(question);
                    current_id = qId;
                }

                int aId = cursor.getInt(6);
                String aText = cursor.getString(7);
                boolean aCorrect = (cursor.getInt(8) == 1);

                String exp = cursor.getString(9);
                if (exp.equals("##")){
                    qExplain = qExplain.replaceFirst("##", String.valueOf(letter));
                } else {
                    qExplain = qExplain + exp;
                    qExplain = qExplain.replaceFirst("@", String.valueOf(letter));
                }

                if (aCorrect) {
                    qExplain = qExplain.replace('%', letter);
                }

                question.setExplanation(qExplain);

                // create answer
                Answer answer = new Answer(aId, aText, aCorrect);
                // add answer to the question's array list
                assert question != null;
                question.addAnswer(answer);
                letter++;
            } while (cursor.moveToNext());
        }
        //close connection
        cursor.close();
        db.close();
        dbHandler.close();

        return questions;
    }

    /**
     * get all questions for test type
     *
     * @param context - app context
     * @param testType - java test, C test, etc.
     * @return list of questions
     */
    public static ArrayList<Question> getAllQuestions(Context context, int testType) {
        String whereClause = " WHERE tq." + DBHandler.COLUMN_TEST + " = " + testType;
        return getQuestions(context, whereClause, "");
    }

    /**
     * get questions by category
     *
     * @param context - app context
     * @param testType - java test, C test, etc.
     * @return list of questions
     */
    public static ArrayList<Question> getQuestionsByCategory(Context context, int testType, int categoryId){
        String whereClause = " WHERE tq." + DBHandler.COLUMN_TEST + " = " + testType +
                " AND tq." + DBHandler.COLUMN_CATEGORY_FK + " = " + categoryId;
        return getQuestions(context, whereClause, "");
    }

    /**
     * get all questions with wrong answer
     *
     * @param context - app context
     * @param testType - java test, C test, etc.
     * @return list of questions
     */
    public static ArrayList<Question> getFailedQuestions(Context context, int testType){
        String whereClause = " INNER JOIN " + DBHandler.TABLE_MISTAKES + " te ON te." +
                DBHandler.COLUMN_ID + " = tq." + DBHandler.COLUMN_ID +
                " WHERE tq." + DBHandler.COLUMN_TEST + " = " + testType;
        return getQuestions(context, whereClause, "");
    }


    /**
     * Add question to mistake table
     *
     * @param questionID - question's id in the database
     */
    public static void addMistake(Context context, int questionID) {
        DBHandler dbHandler = new DBHandler(context);
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHandler.COLUMN_ID, questionID);

        //insert into db (if id is already there - ignore)
        db.insertWithOnConflict(DBHandler.TABLE_MISTAKES, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        //close connection
        db.close();
        dbHandler.close();
    }



    /**
     * Remove question from mistake table
     *
     * @param questionID - question's id in the database
     */
    public static void removeMistake(Context context, int questionID) {
        DBHandler dbHandler = new DBHandler(context);
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String whereClause = DBHandler.COLUMN_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(questionID)};

        //insert into db (if id is already there - ignore)
        db.delete(DBHandler.TABLE_MISTAKES, whereClause, whereArgs);

        //close connection
        db.close();
        dbHandler.close();
    }

    /**
     * Remove all questions from mistake table
     */
    public static void removeAllMistakes(Context context) {
        DBHandler dbHandler = new DBHandler(context);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        // delete all
        db.execSQL("delete from " + DBHandler.TABLE_MISTAKES);
        // free allocated space
        db.execSQL("vacuum");
        //close connection
        db.close();
        dbHandler.close();
    }


    /**
     * Get exact quantity of random questions
     *
     * @param context - app context
     * @param testType - java test, C test, etc.
     * @param questionCount - number of questions to return
     * @return list of questions
     */
    public static ArrayList<Question> getShuffledQuestions(Context context, int testType, int questionCount){
        ArrayList<Question> questions = getAllQuestions(context, testType);
        //shuffle result
        Collections.shuffle(questions);
        for (int i=questionCount; i<questions.size(); i++) {
            questions.remove(i);
        }
        return questions;
    }
}
