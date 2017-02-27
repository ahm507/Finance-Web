package pf.service;

import au.com.bytecode.opencsv.CSVReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;
import pf.user.UserRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Vector;

@RunWith(SpringRunner.class)
@SpringBootTest


public class ExportImportTest {

    @Autowired
    BackupService backup;
    @Autowired
    RestoreService restore;
    @Autowired
    UserRepository userRepo;

    @Autowired
    private ResourceLoader resourceLoader;


//    @Test
//    public void ExportImport() throws Exception {
//
//        String userEmail = "test@test.test";
//
//        UserEntity userEntity = userRepo.findByEmail(userEmail);
//
//        StringWriter buffer = new StringWriter();
//        PrintWriter writer = new PrintWriter(buffer);
//        backup.backup(userEntity.getId(), writer);
//        String exportContents = buffer.toString();
//
//        //Restoring the contents
////        RestoreService restore = Settings.getStoreFactoryForJdbc().createRestore();
//        StringReader stringReader = new StringReader(exportContents);
//        CSVReader reader = new CSVReader(stringReader);
//        Vector<String> out = restore.importFile(reader, userEntity.getId());
//
//    }



    @Test
    @Transactional
    public void importFile() throws Exception {

        String userId = "4f680c93-838d-4329-9aba-7bedca232a89";
        Resource resource = resourceLoader.getResource("classpath:import.csv");
        FileReader file2 = new FileReader(resource.getURL().getPath());
        CSVReader reader = new CSVReader(file2);
        restore.importFile(reader, userId);

        //Verify no exception


    }




}
