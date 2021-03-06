package nf.co.novomic.programlangtest;

/**
 * static methods for formating and casting
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class FormatHelper {

    public static String getTimeString(long milliseconds){
        // get string for minutes
        String minutes = String.format("%02d", (int)(milliseconds / 60000));
        // for seconds
        String seconds = String.format("%02d", (int)((milliseconds/1000)%60));
        return minutes + ":" + seconds;
    }

}
