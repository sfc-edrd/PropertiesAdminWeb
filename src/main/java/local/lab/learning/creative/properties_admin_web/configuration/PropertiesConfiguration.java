package local.lab.learning.creative.properties_admin_web.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class PropertiesConfiguration
{
    @Autowired
    private Environment env;

    private static final String ROOT_PATH = System.getProperty("user.dir");

    private static final String PATH_COMPLEMENT = File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;

    private static final String FULL_PATH = ROOT_PATH + PATH_COMPLEMENT;

    public Properties getProperties()
    {
        return (loadProperties());
    }

    public boolean setProperties(MultiValueMap<String, Object> entries)
    {
        boolean                                     everythingWentFine;
        String                                      line2Write;
        FileOutputStream                            fileOutputStream;
        Iterator<Map.Entry<String, List<Object>>>   entriesIterator;
        Map.Entry<String, List<Object>>             nextEntry;

        everythingWentFine = false;
        line2Write = "";
        fileOutputStream = null;
        try
        {
            if (entries.isEmpty())
                throw new IOException("Empty set of entries");

            fileOutputStream = new FileOutputStream(FULL_PATH + getFileName());
            entriesIterator = entries.entrySet().iterator();
            while (entriesIterator.hasNext())
            {
                nextEntry = entriesIterator.next();
                line2Write = nextEntry.getKey() + "=" + returnArrayDataAsStringWithoutSquareBrackets(nextEntry.getValue());
                fileOutputStream.write(line2Write.getBytes(StandardCharsets.UTF_8));
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            everythingWentFine = true;
        }
        catch (IOException e)
        {
            log.error("Issue Writing to file");
            log.error(e.getMessage());
        }

        return (everythingWentFine);
    }

    private Properties loadProperties()
    {
        Properties  properties;

        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(FULL_PATH + getFileName()))
        {
            properties.load(fileInputStream);
        }
        catch (IOException e)
        {
            log.error("Issue loading file");
            log.error(e.getMessage());
            properties = null;
        }
        finally
        {
            return (properties);
        }
    }

    private String getFileName()
    {
        String[]    activeProfiles;
        String      fileName;

        fileName = "application.properties";
        activeProfiles = env.getActiveProfiles();
        if (activeProfiles.length != 0)
        {
            fileName = "application-" + activeProfiles[0] + ".properties";
        }

        return (fileName);
    }

    private String returnArrayDataAsStringWithoutSquareBrackets(List<Object> arr)
    {
        return (String.format("%s\n", arr.toString().replaceAll("[\\[\\]]", "")));
    }
}
