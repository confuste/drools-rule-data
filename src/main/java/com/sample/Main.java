package com.sample;

import java.io.File;

import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.rule.Pattern;
import org.drools.core.spi.Consequence;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.definition.rule.Rule;
import org.kie.api.definition.KieDefinition.KnowledgeType;
import org.kie.api.definition.KiePackage;

public class Main {

	public static void main(String[] args) {
		
		/* Load rules and Build */
		KieServices kieServices = KieServices.Factory.get();
		
		KieFileSystem kfs = kieServices.newKieFileSystem();
		File file = new File("src/main/resources/rules/Sample.drl");
		Resource resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
		kfs.write( resource);  
		KieBuilder kb = kieServices.newKieBuilder(kfs);
		kb.buildAll();
		 
		/* Init KieSession. It's the the key to access to rules data */
		KieContainer kc = kieServices.newKieClasspathContainer();
		KieSession kSession = kc.newKieSession("ksession-rules");
		
		/* List the KieBase names. Web get the package "rules"
		 * (we have "rules" in our resources folder 
		 */
		System.out.println(kc.getKieBaseNames().toString());
		KieBase kieBase = kc.getKieBase("rules");
		
		/* The code packages are... */
		System.out.println(kieBase.getKiePackages().toString());

		
		/* For each package we get the rules. We can see
		 * that package name in the .drl file header. For example in 
		 * "Sample.drl" the package name is "com.sample".
		 * We are going to show the list with the rules names
		 */
    		for ( KiePackage kp : kieBase.getKiePackages() ) {
		
    			for (Rule rule : kp.getRules()) {
    				System.out.println("Rule name ->" + rule.getName());;
    				//KnowledgeType know = rule.getKnowledgeType();
    				//System.out.println(know);
    			}
    		}
    	
    		/* Now, we can obtain information about a concrete Rule.
    		 * We want to get the rule which is in the drl file with the package name 
    		 * "com.sample" (Sample.drl). The rule will have "Hello World" name
    		 */
    		RuleImpl concreteRule = (RuleImpl) kSession.getKieBase().getRule("com.sample", "Hello World");
    		
    		/* Get the Left Hand Side (LHS) of the "Hello world" rule */
    		System.out.println("LHS: " + concreteRule.getLhs());
    	
    		/* Now, we want to get other information like java Objects that the rule is using
    		 * and variables (identifiers). Also , web want the constraints ("status == Message.HELLO")
    		 */
    		Pattern rce = (Pattern) concreteRule.getLhs().getChildren().get( 0 );   
    		//System.out.println(oneRule.getLhs().getChildren().get( 0 ).getOuterDeclarations().toString());
    		System.out.println(rce);
    		System.out.println("LHS constraints: " + rce.getConstraints());
    	
    		
    		//Consequence cons = concreteRule.getConsequence();
    		//System.out.println(cons.getName());

		
		//kSession.fireAllRules();
		
		 
		
	}

}
