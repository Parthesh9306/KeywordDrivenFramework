package results.jaxb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import results.jaxb.Results.Testcase;

public class XMLGenerator {
/*
    *//**
     * List of test case to store in XML
     *//*
    static List<Results.Testcase> testcases;

    *//**
     * Result to store XML
     * 
     *//*
    static Results results;

    *//**
     * Static block to initialize static variable
     *//*
    static {
	testcases = new ArrayList<Results.Testcase>();
	results = new Results();
    }

    *//**
     * Add test case data to testcase result to write for result XML
     * 
     * @param externalId
     * @param tester
     * @param date
     * @param notes
     * @param result
     *//*
    public static void addTestcaseToList(String externalId, String tester,
	    String date, String notes, String result) {

	Testcase testcase = new Testcase();
	testcase.setExternalId(externalId);
	testcase.setTester(tester);
	testcase.setTimestamp(date);
	testcase.setNotes(notes);
	testcase.setResult(result);

	testcases.add(testcase);
    }

	*//**
	 * Generate XML on specified location
	 * 
	 * @param xmlFileLocation
	 *//*
	public static void generateXML(String xmlFileLocation) {
		if (!testcases.isEmpty()) {
			results.setTestcase(testcases);

			File file = new File(xmlFileLocation);
			JAXBContext jaxbContext;

			try {
				jaxbContext = JAXBContext.newInstance(Results.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);

				jaxbMarshaller.marshal(results, file);
				jaxbMarshaller.marshal(results, System.out);
			} catch (JAXBException e) {
				System.out.println("error while creating xml of result");
				e.printStackTrace();
			}
		}
	}*/
}
