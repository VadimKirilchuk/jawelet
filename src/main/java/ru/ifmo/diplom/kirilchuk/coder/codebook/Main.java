package ru.ifmo.diplom.kirilchuk.coder.codebook;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import ru.ifmo.diplom.kirilchuk.coder.codebook.util.ReadFileHelper;

public class Main {

    /**
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        //example of testing CodeBookGenerator with predefined files
        CodeBookGenerator generator = new CodeBookGenerator();

        URL url = generator.getClass().getResource("filters1");
        File filters = new File(url.toURI());
        ReadFileHelper.readFilters(filters, generator);

        url = generator.getClass().getResource("codebookLL");
        File codebook = new File(url.toURI());
        ReadFileHelper.readData(codebook, generator);

        GeneratorResult result = generator.perform(62);
        // check result by debug or sysouts!
    }

}
