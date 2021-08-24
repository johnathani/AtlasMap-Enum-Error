package com.accenture.atlasmap;

import java.net.URISyntaxException;
import java.net.URL;

import com.accenture.atlasmap.dict.Destination;
import com.accenture.atlasmap.dict.SampleEnum;
import com.accenture.atlasmap.dict.Source;

import io.atlasmap.api.AtlasContext;
import io.atlasmap.api.AtlasContextFactory;
import io.atlasmap.api.AtlasException;
import io.atlasmap.api.AtlasSession;
import io.atlasmap.core.DefaultAtlasContextFactory;

// @Author: Johnathan Ingram (johnathan.ingram@accenture.com)
public class App 
{
    public static void main( String[] args ) throws AtlasException, URISyntaxException
    {
        System.out.println( "Atlasmap Enum Mapping Test");

        URL url = Thread.currentThread().getContextClassLoader().getResource("atlasmap-mapping.adm");
        AtlasContextFactory factory = DefaultAtlasContextFactory.getInstance();
        AtlasContext context = factory.createContext(url.toURI());

        testFullMap(context);
        testNullEnumMap(context);
        testNullIntegerMap(context);
    }

    private static void testFullMap(AtlasContext context) throws AtlasException {
        System.out.println( "Atlasmap Enum Mapping Test: Full mapping with no nulls");
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
        Destination dst = (Destination)session.getDefaultTargetDocument();
        if (!dst.equals(src)) throw new RuntimeException("Object mapping did not work");
        System.out.println( "Atlasmap Enum Mapping Test: Passed full mapping with no nulls");
    }

    private static void testNullEnumMap(AtlasContext context) throws AtlasException {
        System.out.println( "Atlasmap Enum Mapping Test: Full mapping with enum set to null");
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
        Destination dst = (Destination)session.getDefaultTargetDocument();
        if (!dst.equals(src)) throw new RuntimeException("Object mapping did not work");
        System.out.println( "Atlasmap Enum Mapping Test: Full mapping with enum set to null");
    }

    private static void testNullIntegerMap(AtlasContext context) throws AtlasException {
        System.out.println( "Atlasmap Enum Mapping Test: Full mapping with integer set to null");
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
        Destination dst = (Destination)session.getDefaultTargetDocument();
        if (!dst.equals(src)) throw new RuntimeException("Object mapping did not work");
        System.out.println( "Atlasmap Enum Mapping Test: FFull mapping with integer set to null");
    }

}
