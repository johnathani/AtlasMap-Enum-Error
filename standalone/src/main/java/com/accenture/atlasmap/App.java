package com.accenture.atlasmap;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.accenture.atlasmap.dict.Destination;
import com.accenture.atlasmap.dict.DestinationList;
import com.accenture.atlasmap.dict.DestinationObj;
import com.accenture.atlasmap.dict.ListItem;
import com.accenture.atlasmap.dict.Meh;
import com.accenture.atlasmap.dict.SampleEnum;
import com.accenture.atlasmap.dict.Source;
import com.accenture.atlasmap.dict.SourceList;
import com.accenture.atlasmap.dict.SourceObj;

import io.atlasmap.api.AtlasContext;
import io.atlasmap.api.AtlasContextFactory;
import io.atlasmap.api.AtlasException;
import io.atlasmap.api.AtlasSession;
import io.atlasmap.core.DefaultAtlasContextFactory;
import io.atlasmap.v2.Audit;

// @Author: Johnathan Ingram (johnathan.ingram@accenture.com)
public class App {
    public static void main(String[] args) throws AtlasException, URISyntaxException {
        System.out.println("Atlasmap Enum Mapping Test");


        // Emums
        {
            URL url = Thread.currentThread().getContextClassLoader().getResource("atlasmap-mapping.adm");
            AtlasContextFactory factory = DefaultAtlasContextFactory.getInstance();
            AtlasContext context = factory.createContext(url.toURI());

            testEnumFullMap(context);
            testEnumNullEnumMap(context);
            testEnumNullIntegerMap(context);
        }

        // Objects (Complex types)
        {
            URL url = Thread.currentThread().getContextClassLoader().getResource("atlasmap-mapping-obj.adm");
            AtlasContextFactory factory = DefaultAtlasContextFactory.getInstance();
            AtlasContext context = factory.createContext(url.toURI());

            testObjectNullSubObjPartialMap(context);
            testObjectAllNullMap(context);
            testObjectNullMap(context);
        }
/*
        // Lists
        {
            
            URL url = Thread.currentThread().getContextClassLoader().getResource("atlasmap-mapping-list.adm");
            AtlasContextFactory factory = DefaultAtlasContextFactory.getInstance();
            AtlasContext context = factory.createContext(url.toURI());

            testListFullMap(context);
            testListEmptyMap(context);
            testListNullMap(context);
        }
*/

    }

    private static void testEnumFullMap(AtlasContext context) throws AtlasException {
        System.out.println("Atlasmap Enum Mapping Test: Full mapping with no nulls");
        AtlasSession session = context.createSession();

        // Source
        Source src = new Source();
        src.setSourceHeader("Full");
        src.setSourceEnum(SampleEnum.MEDIUM);
        src.setSourceInt(1000);
        src.setSourceNullableInt(2000);

        session.setDefaultSourceDocument(src);

        // Destination
        context.process(session);
        Destination dst = (Destination) session.getDefaultTargetDocument();
        if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
        System.out.println("Atlasmap Enum Mapping Test: Passed full mapping with no nulls");
    }

    private static void testEnumNullEnumMap(AtlasContext context) throws AtlasException {
        System.out.println("Atlasmap Enum Mapping Test: Full mapping with enum set to null");
        AtlasSession session = context.createSession();

        // Source
        Source src = new Source();
        src.setSourceHeader("Full");
        src.setSourceEnum(null);
        src.setSourceInt(1000);
        src.setSourceNullableInt(2000);

        session.setDefaultSourceDocument(src);

        // Destination
        context.process(session);

        // Simulate what is happening in Camel inegration
        List<Audit> errors = new ArrayList<>();
        for (Audit audit : session.getAudits().getAudit()) {
            String aVsal = audit.getValue();
            switch (audit.getStatus()) {
                case ERROR:
                    System.out.println(
                            "Error .. this will raise an exception .. which the null enum mappin generates an error");
                    throw new RuntimeException("Object mapping did not work");
                case WARN:
                    System.out.println("Warn");
                    break;
                default:
                    System.out.println("Default");
                    break;
            }
        }

        Destination dst = (Destination) session.getDefaultTargetDocument();
        if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
        System.out.println("Atlasmap Enum Mapping Test: Passed full mapping with enum set to null");
    }

    private static void testEnumNullIntegerMap(AtlasContext context) throws AtlasException {
        System.out.println("Atlasmap Enum Mapping Test: Full mapping with integer set to null");
        AtlasSession session = context.createSession();

        // Source
        Source src = new Source();
        src.setSourceHeader("Full");
        src.setSourceEnum(SampleEnum.LOW);
        src.setSourceInt(1000);
        src.setSourceNullableInt(null);

        session.setDefaultSourceDocument(src);

        // Destination
        context.process(session);
        Destination dst = (Destination) session.getDefaultTargetDocument();
        if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
        System.out.println("Atlasmap Enum Mapping Test: Passed full mapping with integer set to null");
    }

    private static void testListFullMap(AtlasContext context) throws AtlasException {
        System.out.println("Atlasmap List Mapping Test: Full mapping");
        AtlasSession session = context.createSession();

        // Source
        SourceList src = new SourceList();
        src.setNoise1("Noisy");
        src.setNoise2("List");
        src.setFound(IntStream.range(1, 11).mapToObj(i -> {
            Meh item = new Meh();
            item.setMyField(i + "x values");
            item.setSample(SampleEnum.LOW);

            ListItem li = new ListItem();
            li.setName("Number: " + i);
            li.setOrd(i);
            li.setValue(item);

            return li;
        }).collect(Collectors.toList()));

        session.setDefaultSourceDocument(src);

        // Destination
        context.process(session);

        // Simulate what is happening in Camel inegration
        List<Audit> errors = new ArrayList<>();
        for (Audit audit : session.getAudits().getAudit()) {
            String aVsal = audit.getValue();
            switch (audit.getStatus()) {
                case ERROR:
                    System.out.println("Error ...");
                    throw new RuntimeException("Object mapping did not work");
                case WARN:
                    System.out.println("Warn");
                    break;
                default:
                    System.out.println("Default");
                    break;
            }
        }



        DestinationList dst = (DestinationList) session.getDefaultTargetDocument();

        if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
        System.out.println("Atlasmap List Mapping Test: Passed full mapping");
    }
    

    private static void testListEmptyMap(AtlasContext context) throws AtlasException {
        System.out.println("Atlasmap List Mapping Test: Empty mapping");
        AtlasSession session = context.createSession();

        // Source
        SourceList src = new SourceList();
        src.setNoise1("Noisy");
        src.setNoise2("List");
        src.setFound(Collections.EMPTY_LIST);

        session.setDefaultSourceDocument(src);

        // Destination
        context.process(session);

        // Simulate what is happening in Camel inegration
        List<Audit> errors = new ArrayList<>();
        for (Audit audit : session.getAudits().getAudit()) {
            String aVsal = audit.getValue();
            switch (audit.getStatus()) {
                case ERROR:
                    System.out.println("Error ...");
                    throw new RuntimeException("Object mapping did not work");
                case WARN:
                    System.out.println("Warn");
                    break;
                default:
                    System.out.println("Default");
                    break;
            }
        }



        DestinationList dst = (DestinationList) session.getDefaultTargetDocument();

        if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
        System.out.println("Atlasmap List Mapping Test: Passed empty mapping");
    }

    private static void testListNullMap(AtlasContext context) throws AtlasException {
        System.out.println("Atlasmap List Mapping Test: Null mapping");
        AtlasSession session = context.createSession();

        // Source
        SourceList src = new SourceList();
        src.setNoise1("Noisy");
        src.setNoise2("List");
        src.setFound(null);

        session.setDefaultSourceDocument(src);

        // Destination
        context.process(session);

        // Simulate what is happening in Camel inegration
        List<Audit> errors = new ArrayList<>();
        for (Audit audit : session.getAudits().getAudit()) {
            String aVsal = audit.getValue();
            switch (audit.getStatus()) {
                case ERROR:
                    System.out.println("Error ...");
                    throw new RuntimeException("Object mapping did not work");
                case WARN:
                    System.out.println("Warn");
                    break;
                default:
                    System.out.println("Default");
                    break;
            }
        }



        DestinationList dst = (DestinationList) session.getDefaultTargetDocument();

        if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
        System.out.println("Atlasmap List Mapping Test: Passed null mapping");
    }

    private static void testObjectNullSubObjPartialMap(AtlasContext context) throws AtlasException {
        System.out.println("Atlasmap List Mapping Test: Partial Null mapping");
        AtlasSession session = context.createSession();

        // Source
        Meh item = new Meh();
        item.setMyField("Inner object value set, outer object should be created");
        item.setSample(null);

        SourceObj src = new SourceObj();
        src.setSourceName(null);
        src.setMeh(item);

        session.setDefaultSourceDocument(src);

        // Destination
        context.process(session);

        // Simulate what is happening in Camel inegration
        List<Audit> errors = new ArrayList<>();
        for (Audit audit : session.getAudits().getAudit()) {
            String aVsal = audit.getValue();
            switch (audit.getStatus()) {
                case ERROR:
                    System.out.println("Error ...");
                    throw new RuntimeException("Object mapping did not work");
                case WARN:
                    System.out.println("Warn");
                    break;
                default:
                    System.out.println("Default");
                    break;
            }
        }



        DestinationObj dst = (DestinationObj) session.getDefaultTargetDocument();

        if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
        System.out.println("Atlasmap Object Mapping Test: Passed partial null mapping");
    } 

    private static void testObjectAllNullMap(AtlasContext context) throws AtlasException {
        System.out.println("Atlasmap List Mapping Test: All null mapping");
        AtlasSession session = context.createSession();

        // Source
        SourceObj src = new SourceObj();
        src.setSourceName(null);
        src.setMeh(null);

        session.setDefaultSourceDocument(src);

        // Destination
        context.process(session);

        // Simulate what is happening in Camel inegration
        List<Audit> errors = new ArrayList<>();
        for (Audit audit : session.getAudits().getAudit()) {
            String aVsal = audit.getValue();
            switch (audit.getStatus()) {
                case ERROR:
                    System.out.println("Error ...");
                    throw new RuntimeException("Object mapping did not work");
                case WARN:
                    System.out.println("Warn");
                    break;
                default:
                    System.out.println("Default");
                    break;
            }
        }



        DestinationObj dst = (DestinationObj) session.getDefaultTargetDocument();

        if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
        System.out.println("Atlasmap Object Mapping Test: Passed all null mapping");
    } 

    private static void testObjectNullMap(AtlasContext context) throws AtlasException {
        System.out.println("Atlasmap Object Mapping Test: Null mapping");
        AtlasSession session = context.createSession();

        // Source
        SourceObj src = new SourceObj();
        src.setSourceName("Testing");
        src.setMeh(null);

        session.setDefaultSourceDocument(src);

        // Destination
        context.process(session);

        // Simulate what is happening in Camel inegration
        List<Audit> errors = new ArrayList<>();
        for (Audit audit : session.getAudits().getAudit()) {
            String aVsal = audit.getValue();
            switch (audit.getStatus()) {
                case ERROR:
                    System.out.println("Error ...");
                    throw new RuntimeException("Object mapping did not work");
                case WARN:
                    System.out.println("Warn");
                    break;
                default:
                    System.out.println("Default");
                    break;
            }
        }



        DestinationObj dst = (DestinationObj) session.getDefaultTargetDocument();

        if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
        System.out.println("Atlasmap Object Mapping Test: Passed null mapping");
    }

    

}
