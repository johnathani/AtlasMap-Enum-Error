package com.accenture.atlasmap.route;

import java.io.File;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.accenture.atlasmap.dict.Destination;
import com.accenture.atlasmap.dict.DestinationList;
import com.accenture.atlasmap.dict.ListItem;
import com.accenture.atlasmap.dict.Meh;
import com.accenture.atlasmap.dict.SampleEnum;
import com.accenture.atlasmap.dict.Source;
import com.accenture.atlasmap.dict.SourceList;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import io.atlasmap.api.AtlasContext;
import io.atlasmap.api.AtlasContextFactory;
import io.atlasmap.api.AtlasSession;
import io.atlasmap.core.DefaultAtlasContextFactory;

@Component
public class AtlasMapRoute extends RouteBuilder {
   @Override
   public void configure() throws Exception {
      rest().path("sample").get("full").bindingMode(RestBindingMode.off).to("direct:mapAtlasFull");

      rest().path("sample").get("null").bindingMode(RestBindingMode.off).to("direct:mapAtlasNull");

      rest().path("sample").get("manual").bindingMode(RestBindingMode.off).to("direct:mapAtlasManual");

      rest().path("sample").get("list").bindingMode(RestBindingMode.off).to("direct:listAtlasFull");

      from("direct:mapAtlasFull").log(LoggingLevel.INFO, "Atlasmap Enum Mapping Test: Full mapping with no nulls")
            .process(e -> {
               // Source
               Source src = new Source();
               src.setSourceHeader("Full");
               src.setSourceEnum(SampleEnum.MEDIUM);
               src.setSourceInt(1000);
               src.setSourceNullableInt(2000);

               e.setProperty("Source", src); // Store the original on the exchange
               e.getIn().setBody(src);
            }).to("atlasmap:atlasmap-mapping.adm").process(e -> {
               Source src = (Source) e.getProperty("Source");

               Destination dst = (Destination) e.getIn().getBody();
               if (!dst.equals(src))
                  throw new RuntimeException("Object mapping did not work");
            }).log(LoggingLevel.INFO, "Atlasmap Enum Mapping Test: Passed full mapping with no nulls").transform()
            .constant("Passed full mapping with no nulls");

      from("direct:mapAtlasNull").log(LoggingLevel.INFO, "Atlasmap Enum Mapping Test: Full mapping with nulls")
            .process(e -> {
               // Source
               Source src = new Source();
               src.setSourceHeader("Full");
               src.setSourceEnum(null);
               src.setSourceInt(1000);
               src.setSourceNullableInt(2000);

               e.setProperty("Source", src); // Store the original on the exchange
               e.getIn().setBody(src);
            }).to("atlasmap:atlasmap-mapping.adm").process(e -> {
               Source src = (Source) e.getProperty("Source");

               Destination dst = (Destination) e.getIn().getBody();
               if (!dst.equals(src))
                  throw new RuntimeException("Object mapping did not work");
            }).log(LoggingLevel.INFO, "Atlasmap Enum Mapping Test: Passed full mapping with nulls").transform()
            .constant("Passed full mapping with nulls");

      from("direct:mapAtlasManual")
            .log(LoggingLevel.INFO, "Atlasmap Enum Mapping Test: Full mapping with nulls without Camel integration")
            .process(e -> {
               // Source
               Source src = new Source();
               src.setSourceHeader("Full");
               src.setSourceEnum(null);
               src.setSourceInt(1000);
               src.setSourceNullableInt(2000);

               e.setProperty("Source", src); // Store the original on the exchange
               e.getIn().setBody(src);
            }).process(e -> {
               try {
                  AtlasContextFactory factory = DefaultAtlasContextFactory.getInstance();
                  AtlasContext context;
                  if (null != System.getProperty("adm-location")) {
                     File file = new File(System.getProperty("adm-location"));
                     context = factory.createContext(file);
                  } else {
                     URL url = Thread.currentThread().getContextClassLoader().getResource("atlasmap-mapping.adm");
                     context = factory.createContext(url.toURI());
                  }

                  AtlasSession session = context.createSession();

                  Object src = e.getIn().getBody();
                  session.setDefaultSourceDocument(src);

                  // Destination
                  context.process(session);
                  e.getIn().setBody(session.getDefaultTargetDocument());
               } catch (Exception ex) {
                  throw new Exception("Unable to run atlasmap", ex);
               }

            }).process(e -> {
               Source src = (Source) e.getProperty("Source");

               Destination dst = (Destination) e.getIn().getBody();
               if (!dst.equals(src))
                  throw new RuntimeException("Object mapping did not work");
            })
            .log(LoggingLevel.INFO,
                  "Atlasmap Enum Mapping Test: Passed full mapping with nulls without Camel integration")
            .transform().constant("Passed full mapping with nulls without Camel integration");

      from("direct:listAtlasFull").log(LoggingLevel.INFO, "Atlasmap List Mapping Test: Full mapping").process(e -> {
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

         e.setProperty("Source", src); // Store the original on the exchange
         e.getIn().setBody(src);
      }).to("atlasmap:atlasmap-mapping-list.adm").process(e -> {
         SourceList src = (SourceList) e.getProperty("Source");

         DestinationList dst = (DestinationList) e.getIn().getBody();
         if (!dst.equals(src))
            throw new RuntimeException("Object mapping did not work");
      }).log(LoggingLevel.INFO, "Atlasmap List Mapping Test: Passed full mapping").transform()
            .constant("Passed full list mapping");

   }
}
