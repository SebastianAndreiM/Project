package com.example.userservice.export;

import com.example.userservice.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.util.List;

public class UserExportFactory {
    public static ByteArrayInputStream export(String type, List<User> users) throws IOException {
        return switch (type) {
            case "csv" -> exportCSV(users);
            case "json" -> exportJSON(users);
            case "xml" -> exportXML(users);
            case "doc" -> exportDOC(users);
            default -> throw new IllegalArgumentException("Unknown export type");
        };
    }

    private static ByteArrayInputStream exportCSV(List<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,username,email,phone,role\n");
        for (User u : users) {
            sb.append(String.format("%d,%s,%s,%s,%s\n",
                    u.getId(), u.getUsername(), u.getEmail(), u.getPhone(), u.getRole()));
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    private static ByteArrayInputStream exportJSON(List<User> users) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, users);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportXML(List<User> users) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlMapper.writeValue(out, users);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportDOC(List<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista useri:\n\n");
        for (User u : users) {
            sb.append("Username: ").append(u.getUsername()).append("\n");
            sb.append("Email: ").append(u.getEmail()).append("\n");
            sb.append("Telefon: ").append(u.getPhone()).append("\n");
            sb.append("Rol: ").append(u.getRole()).append("\n\n");
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }
}
