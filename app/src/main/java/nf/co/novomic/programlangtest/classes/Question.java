package nf.co.novomic.programlangtest.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Question entity
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class Question implements Parcelable{

    private int id;
    private String questionText;
    private int categoryId;
    private ArrayList<Answer> answers;
    private boolean multipleChoice;
    private int testType;
    private String explanation;

    // values of the testType field
    public static final int JAVA_TEST = 1;
    public static final int C_TEST = 2;
    public static final int C_PLUS = 3;
    public static final int C_SHARP = 4;

    /**
     * Question constructor method
     *
     * params are all attributes of the class
     *
     * @param id - question id in the data store
     * @param questionText - text of the question
     * @param categoryId - test category
     * @param answers - array of possible answers
     * @param multipleChoice - true if multiple choice question
     * @param testType - type: Java test, C test etc.
     */
    public Question(int id, String questionText, int categoryId,
                    ArrayList<Answer> answers, boolean multipleChoice,int testType,
                    String explanation){

        this.id = id;
        this.questionText = questionText;
        this.categoryId = categoryId;
        this.answers = answers;
        this.multipleChoice = multipleChoice;
        this.testType = testType;
        this.explanation = explanation;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        questionText = in.readString();
        categoryId = in.readInt();
        answers = in.createTypedArrayList(Answer.CREATOR);
        testType = in.readInt();
        explanation = in.readString();
        multipleChoice = (in.readByte() == 1);
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    /**
     *
     * @return array list of posible answers
     */
    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    /**
     * Add answer to question's answer array
     *
     * @param answer Answer object to add
     */
    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    /**
     *
     * @return question category id
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     *
     * @return question id in the database
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return true if question has more then one right answer
     */
    public boolean isMultipleChoice() {
        return multipleChoice;
    }

    /**
     *
     * @return text of the question
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     *
     * @return type (java/c/...)
     */
    public int getTestType() {
        return testType;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    //PARCELABLE implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(questionText);
        dest.writeInt(categoryId);
        dest.writeTypedList(answers);
        dest.writeInt(testType);
        dest.writeString(explanation);
        dest.writeByte((byte)(multipleChoice ? 1 : 0));
    }


}
