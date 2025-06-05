package com.example.productservice.export;

import com.example.productservice.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.util.List;

public class ProductExportFactory {
    // Pattern: Factory Method
    public static ByteArrayInputStream export(String type, List<Product> products) throws IOException {
        return switch (type) {
            case "csv" -> exportCSV(products);
            case "json" -> exportJSON(products);
            case "xml" -> exportXML(products);
            case "doc" -> exportDOC(products);
            default -> throw new IllegalArgumentException("Unknown export type");
        };
    }

    private static ByteArrayInputStream exportCSV(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,model,producer,category,description,imageUrl,price\n");
        for (Product p : products) {
            sb.append(String.format("%d,%s,%s,%s,%s,%s,%.2f\n",
                    p.getId(), p.getModel(), p.getProducer(), p.getCategory(),
                    p.getDescription(), p.getImageUrl(), p.getPrice()));
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    private static ByteArrayInputStream exportJSON(List<Product> products) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, products);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportXML(List<Product> products) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlMapper.writeValue(out, products);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportDOC(List<Product> products) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista produse:\n\n");
        for (Product p : products) {
            sb.append("Model: ").append(p.getModel()).append("\n");
            sb.append("Producător: ").append(p.getProducer()).append("\n");
            sb.append("Categorie: ").append(p.getCategory()).append("\n");
            sb.append("Descriere: ").append(p.getDescription()).append("\n");
            sb.append("Imagine: ").append(p.getImageUrl()).append("\n");
            sb.append("Preț: ").append(p.getPrice()).append("\n\n");
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }
}
