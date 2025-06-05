package org.example.storeservice.export;

import org.example.storeservice.model.Store;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.util.List;

public class StoreExportFactory {
    // Pattern: Factory Method
    public static ByteArrayInputStream export(String type, List<Store> stores) throws IOException {
        return switch (type) {
            case "csv" -> exportCSV(stores);
            case "json" -> exportJSON(stores);
            case "xml" -> exportXML(stores);
            case "doc" -> exportDOC(stores);
            default -> throw new IllegalArgumentException("Unknown export type");
        };
    }

    private static ByteArrayInputStream exportCSV(List<Store> stores) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,location,phone,email,logoUrl\n");
        for (Store s : stores) {
            sb.append(String.format("%d,%s,%s,%s,%s,%s\n",
                    s.getId(), s.getName(), s.getLocation(),
                    s.getPhone(), s.getEmail(), s.getLogoUrl()));
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    private static ByteArrayInputStream exportJSON(List<Store> stores) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, stores);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportXML(List<Store> stores) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlMapper.writeValue(out, stores);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportDOC(List<Store> stores) {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista magazine:\n\n");
        for (Store s : stores) {
            sb.append("Nume: ").append(s.getName()).append("\n");
            sb.append("Locație: ").append(s.getLocation()).append("\n");
            sb.append("Telefon: ").append(s.getPhone()).append("\n");
            sb.append("Email: ").append(s.getEmail()).append("\n");
            sb.append("Logo: ").append(s.getLogoUrl()).append("\n\n");
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }
}
