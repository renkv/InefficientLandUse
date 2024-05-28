package com.land.sys.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class YamlReader {
    public static void main(String[] args) throws Exception {
        Yaml yaml = new Yaml();
        try{
            FileInputStream inputStream = new FileInputStream("application.yml");
            Map<String,Object> data = yaml.load(inputStream);
            String url = (String)data.get("token.url");
            System.out.println(url);
        }catch (Exception e){
            e.getMessage();
        }
    /*    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<String, Object> config = mapper.readValue(new File("config.yml"), Map.class);

        String value = (String) config.get("key");
        System.out.println(value);*/
    }
}
