package com.epam.mentoring.highload;

import junit.framework.TestCase;
import org.junit.Test;

import java.net.URISyntaxException;

public class WordCountTaskTest
    extends TestCase
{
    @Test
    public void test() throws URISyntaxException {
        String inputFile = getClass().getResource("/wordcount.txt").toURI().toString();
        new WordCountTask().run(inputFile);
    }
}
