package com.mir.ven;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBClient 
{
	public static void main(String[] args) {
		JAXBContext context = null;
		List<Person> list = null;
		Person person1 = null;
		Person person2 = null;
		Family family = null;
		Marshaller marshaller = null;
		
		try {
			context = JAXBContext.newInstance(Person.class);
			list = new ArrayList<Person>();
			
			person1 = new Person();
			family = new Family();
			
			family.setDescription("Family Mimul");
			person1.setFirstName("IlDong");
			person1.setLastName("Hong");
			person1.setFamily(family);
			list.add(person1);

			person2 = new Person();
			person2.setFirstName("LeeDong");
			person2.setLastName("Hong");
			person2.setFamily(family);
			list.add(person2);

			// Marshal 객체를 XML로 변환
			// Unmarshal the objects from XML

			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");  
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, 
			   Boolean.TRUE); 
			
			marshaller.marshal(person2, System.out);
		
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}