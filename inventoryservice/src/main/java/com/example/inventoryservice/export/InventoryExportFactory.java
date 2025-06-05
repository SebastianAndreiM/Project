package com.example.inventoryservice.export;

import com.example.inventoryservice.model.Inventory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.util.List;

public class InventoryExportFactory {
    public static ByteArrayInputStream export(String type, List<Inventory> inventories) throws IOException {
        return switch (type) {
            case "csv" -> exportCSV(inventories);
            case "json" -> exportJSON(inventories);
            case "xml" -> exportXML(inventories);
            case "doc" -> exportDOC(inventories);
            default -> throw new IllegalArgumentException("Unknown export type");
        };
    }

    private static ByteArrayInputStream exportCSV(List<Inventory> inventories) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,storeId,productId,size,color,stock\n");
        for (Inventory i : inventories) {
            sb.append(String.format("%d,%d,%d,%s,%s,%d\n",
                    i.getId(), i.getStoreId(), i.getProductId(), i.getSize(), i.getColor(), i.getStock()));
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    private static ByteArrayInputStream exportJSON(List<Inventory> inventories) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, inventories);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportXML(List<Inventory> inventories) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlMapper.writeValue(out, inventories);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportDOC(List<Inventory> inventories) {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista stocuri:\n\n");
        for (Inventory i : inventories) {
            sb.append("Magazin (storeId): ").append(i.getStoreId()).append("\n");
            sb.append("Produs (productId): ").append(i.getProductId()).append("\n");
            sb.append("Mărime: ").append(i.getSize()).append("\n");
            sb.append("Culoare: ").append(i.getColor()).append("\n");
            sb.append("Stoc: ").append(i.getStock()).append("\n\n");
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }
}
