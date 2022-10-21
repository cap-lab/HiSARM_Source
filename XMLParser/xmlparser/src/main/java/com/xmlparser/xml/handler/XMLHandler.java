package java.com.xmlparser.xml.handler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

public abstract class XMLHandler {
    protected abstract void storeResource(StringWriter writer) throws Exception;

    protected abstract void loadResource(ByteArrayInputStream is) throws Exception;

    public abstract void init();

    public void loadXMLfileToHandler(String xmlFileName) throws Exception {
        String xmlData = getLocalFile(xmlFileName);
        if (xmlData == null || xmlData.indexOf(xmlFileName + " doesn't exist") == 0) {
            throw new Exception("[ERROR] file doesn't exist: " + xmlFileName);
        }
        ByteArrayInputStream is = new ByteArrayInputStream(xmlData.getBytes());
        loadResource(is);
        init();
    }

    public void setXMLString(String xmlString) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(xmlString.getBytes());
        loadResource(is);
    }

    public String getXMLString() throws Exception {
        StringWriter writer = new StringWriter();
        storeResource(writer);
        writer.flush();
        return writer.toString();
    }

    public void storeXMLString(String fileName) throws Exception {
        StringWriter writer = new StringWriter();
        storeResource(writer);
        writer.flush();

        FileOutputStream os;
        try {
            os = new FileOutputStream(fileName);
            byte[] abContents = writer.toString().getBytes();
            os.write(abContents, 0, abContents.length);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLocalFile(String filename) {
        File file = new File(filename);
        if (!file.exists())
            return null;

        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            return new String(buffer);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean putLocalFile(String filename, String data) {
        File file = new File(filename);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
