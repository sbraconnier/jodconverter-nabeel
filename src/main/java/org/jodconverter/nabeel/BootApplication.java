package org.jodconverter.nabeel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import org.jodconverter.DocumentConverter;

/**
 * Main application.
 */
@SpringBootApplication
public class BootApplication {

    /**
     * Main entry point of the application.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @Bean
    public CommandLineRunner convert(DocumentConverter converter) {
        return args -> {

            final Path targetDirPath = Files.createTempDirectory("jod_nabeel");
            final File targetDirFile = targetDirPath.toFile();
            for (int i = 1; i <= 25; i++) {
                final String filename = String.format("test%02d.doc", i);
                final File source = ResourceUtils.getFile("classpath:documents/" + filename);
                final File target = new File(targetDirFile, filename + ".pdf");

                converter.convert(source).to(target).execute();
            }

            // Delete temp directory.
            Files.walk(targetDirPath, 1)
                    .filter(Files::isRegularFile)
                    .forEach(path -> path.toFile().delete());
            Files.delete(targetDirPath);
        };
    }
}
