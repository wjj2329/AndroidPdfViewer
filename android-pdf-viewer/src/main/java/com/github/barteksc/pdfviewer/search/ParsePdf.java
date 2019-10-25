package com.github.barteksc.pdfviewer.search;

import android.os.AsyncTask;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class ParsePdf {

    boolean check(String s) {
        if (s == null) // checks if the String is null {
            return false;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            // checks whether the character is not a letter
            // if it is not a letter ,it will return false
            if ((Character.isLetter(s.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    Dictionary dictionary = new Dictionary();

    class GetCharLocationAndSize extends PDFTextStripper {
        public GetCharLocationAndSize() throws IOException {
        }

        /**
         * @throws IOException If there is an error parsing the document.
         */

        /**
         * Override the default functionality of PDFTextStripper.writeString()
         */
        @Override
        protected void writeString(String string, List<TextPosition> textPositions) throws IOException {

            String[] words = string.split("\\s+");
            int startingPos = 0;
            for (String word : words) {
                if (word.length() > 0) {
                    System.out.println("I check this word " + word);
                    TextPosition startingLet = textPositions.get(startingPos);
                    startingPos += word.length();
                    TextPosition endingLet = textPositions.get(startingPos - 1);
                    System.out.println("start is "+startingLet.getUnicode()+" end is "+endingLet.getUnicode());
                    word = word.replaceAll("[^a-zA-Z]", "");
                    System.out.println("I add " + word.toLowerCase());
                    dictionary.addWord(word.toLowerCase());
                }
            }
            /*
            for (TextPosition text : textPositions) {
                System.out.println(text.getUnicode() + " [(X=" + text.getXDirAdj() + ",Y=" +
                        text.getYDirAdj() + ") height=" + text.getHeightDir() + " width=" +
                        text.getWidthDirAdj() + "]");
            }
            */

        }
    }

    private class ParseTask extends AsyncTask<File, Void, Boolean> {

        @Override
        protected Boolean doInBackground(File... objects) {
            PDDocument document = null;
            try {
                document = PDDocument.load(objects[0]);
                PDFTextStripper stripper = new GetCharLocationAndSize();
                stripper.setSortByPosition(true);
                stripper.setStartPage(0);
                stripper.setEndPage(document.getNumberOfPages());
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                stripper.writeText(document, dummy);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (document != null) {
                    try {
                        document.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public void parsePdf(File file) {
        ParseTask parseTask = new ParseTask();
        parseTask.execute(file);
    }
}
