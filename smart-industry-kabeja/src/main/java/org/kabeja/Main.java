/*
 Copyright 2005 Simon Mieth

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.kabeja;

import org.kabeja.dxf.DXFDocument;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import org.kabeja.processing.ProcessPipeline;
import org.kabeja.processing.ProcessingManager;
import org.kabeja.tools.SAXProcessingManagerBuilder;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 */
public class Main {
    private String encoding = DXFParser.DEFAULT_ENCODING;
    private Parser parser;
    private ProcessingManager processorManager;
    private String pipeline;

    public Main() {
    }

    public static void main(String[] args) throws FileNotFoundException {
        Main main = new Main();
        //main.parseFile("E:/tmp/draft1.dxf");
        //return;
        //InputStream fi = Thread.currentThread().getContextClassLoader().getResourceAsStream();

        main.process();
        String dxfName = "test";
        String fileName = dxfName+".dxf";
        //String fileName = dxfName+".dwg";
        File f = ResourceUtils.getFile("classpath:samples/dxf/"+ fileName);
        File outFile = new File("E:/tmp");
        if(!outFile.exists()){
            outFile.mkdir();
        }
        String output = "E:/tmp/"+dxfName+".svg";
        if (f.exists() && f.isFile()) {
            main.parseFile(f, output);
        }
    }

    public void setPipeline(String name) {
        this.pipeline = name;
    }

    public void initialize() {
        if (this.processorManager == null) {
            this.setProcessConfig(this.getClass().getResourceAsStream(
                    "/conf/process.xml"));
        }
    }

    public void process() {
        if (parser == null) {
            parser = ParserBuilder.createDefaultParser();
        }
        setPipeline("svg");
        initialize();
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 解析dxf文件，转换成svg
     *
     * @param f
     * @param output
     */
    public void parseFile(File f, String output) {
        try {
            this.parser.parse(new FileInputStream(f), encoding);

            DXFDocument doc = parser.getDocument();
            this.processorManager.process(doc, new HashMap(),
                    this.pipeline, new FileOutputStream(output));

            // TODO move this into the svg block + gzip
            // else {
            // OutputStream out = null;
            //
            // out = new FileOutputStream(output);
            //
            // SAXPrettyOutputter writer = new SAXPrettyOutputter(out,
            // SAXPrettyOutputter.DEFAULT_ENCODING);

            // if (this.outputDTD) {
            // writer.setDTD(SVGConstants.SVG_DTD_1_0);
            // }
            // SAXGenerator gen = new SVGGenerator();
            // gen.setProperties(new HashMap());
            // gen.generate(doc, writer);
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProcessConfig(InputStream in) {
        this.processorManager = SAXProcessingManagerBuilder.buildFromStream(in);
    }

    public void printPipelines() {
        Iterator i = this.processorManager.getProcessPipelines().keySet()
                .iterator();
        System.out.println("\n Available pipelines:\n----------\n");

        while (i.hasNext()) {
            String pipeline = (String) i.next();
            ProcessPipeline pp = this.processorManager
                    .getProcessPipeline(pipeline);
            System.out.print(" " + pipeline);
            if (pp.getDescription().length() > 0) {
                System.out.print("\t" + pp.getDescription());

            }
            System.out.println();
        }
    }
}
