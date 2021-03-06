package nf.co.novomic.programlangtest;


import nf.co.novomic.programlangtest.classes.Question;

/**
 * Test simulation activity class
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public interface QuestionCallbacks {

    /**
     * Starts next question
     * If question was the last, show results
     *
     * For the next question fragment Question object
     * will be attached
     *
     * Also updates stat row of activity
     * (set current question and number of errors)
     *
     * @param isCorrect - flag, if current question was answered right
     */
    void startNextQuestion(boolean isCorrect);

    /**
     * Get current question for a fragment
     *
     * @return question object
     */
    Question getQuestion();

    /**
     * Re-create current activity with default settings
     */
    void RestartActivity();

    /**
     * Is the last question
     *
     * @return boolean
     */
    boolean getLast();

}
