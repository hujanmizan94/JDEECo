/*
 * generated by Xtext
 */
package cz.cuni.mff.d3s.jdeeco.edl.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.*
import cz.cuni.mff.d3s.jdeeco.edl.utils.*
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import java.util.HashMap
import java.util.Map

/**
 * Generates code from your model files on save.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#TutorialCodeGeneration
 */
class EDLGenerator implements IGenerator, ITypeResolutionContext {
	
	Map<String, TypeDefinition> dataTypes;
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		dataTypes = new HashMap();
		
		var document = resource.contents.filter(typeof(EdlDocument)).findFirst[true];
		var allParts = document.package.toParts()			
		
		var packageString = String.join(".", allParts);
		var path = String.join("/", allParts) + "/";		
		
		for(TypeDefinition d : document.knowledgeTypes) {
			generateType(d, fsa, path, packageString)
			dataTypes.put(d.name, d);
		}
		
		for(DataContractDefinition d : document.dataContracts) {
			generateDataContract(d, fsa, path, packageString)
			dataTypes.put(d.name, d);
		}
		
		for(EnsembleDefinition e : document.ensembles) {
			generateEnsemble(e, fsa, path, packageString);			
		}
	}
	
	def void generateEnsemble(EnsembleDefinition e, IFileSystemAccess fsa, String path, String packageString) {
		fsa.generateFile(path+e.name + ".java", 
			
'''package «packageString»;

import java.util.ArrayList;
import java.util.List;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;

public class «e.name» implements EnsembleInstance {
	// Ensemble ID
	public «getJavaTypeName(e.id.type.name)» «e.id.fieldName»;
	
	public «e.name»(«getJavaTypeName(e.id.type.name)» «e.id.fieldName») {
		this.«e.id.fieldName» = «e.id.fieldName»;
		«FOR r : e.roles»
		«IF r.cardinalityMax != 1»		
		«r.name» = new ArrayList<>();
		«ENDIF»		
		«ENDFOR»
	}
	
	// Aliases
	«FOR a : e.aliases»	
	public «getJavaTypeName(EDLUtils.getType(this, a.aliasValue, e))» «a.aliasId»() {
		return «a.aliasValue.accept(new ToStringVisitor())»;
	}
		
	«ENDFOR»		  
	
	// Ensemble roles		
	«FOR r : e.roles»
	«IF r.cardinalityMax == 1»
	public «r.type.name» «r.name»;
	«ELSE»
	public List<«r.type.name»> «r.name»;
	«ENDIF»
	«ENDFOR»

	// Knowledge exchange

	@Override
	public void performKnowledgeExchange() {
		«FOR rule : e.exchangeRules»
		«var role = e.roles.findFirst[it.name.equals(rule.field.toParts().get(0))]»
		«IF (role.cardinalityMax != 1)»
		for («role.type.toString()» x : «role.name») {
			x.«String.join(".", rule.field.toParts().drop(1))» = «rule.query.accept(new ToStringVisitor())»;
		} 
		«ELSE»
		«rule.field.toString()» = «rule.query.accept(new ToStringVisitor())»;
		«ENDIF»				
		«ENDFOR»		
	}		
}'''
			);
	}
	
	def void generateDataContract(DataContractDefinition d, IFileSystemAccess fsa, String path, String packageString) {
		val containsId = d.fields.exists[it.name.equals("id") && it.type.equals("string")];
		
		fsa.generateFile(path+d.name + ".java", 
			
'''package «packageString»;

import cz.cuni.mff.d3s.deeco.annotations.Role;

@Role
public class «d.name» {	
	«IF !containsId»
	public String id;
	«ENDIF»
	«FOR f : d.fields»					
	public «getJavaTypeName(f.type.name)» «f.name»;				
	«ENDFOR»				
}'''
			);
	}
	
	def void generateType(TypeDefinition d, IFileSystemAccess fsa, String path, String packageString) {
		fsa.generateFile(path+d.name + ".java", 
			
'''package «packageString»;

public class «d.name» {	
	«FOR f : d.fields»				
	public «getJavaTypeName(f.type.name)» «f.name»;				
	«ENDFOR»				
}'''
			);
	}
	
	def String getJavaTypeName(String type) {
		switch type {
			case "string":
				"String"
			case "bool":
				"boolean"
			default:
				type
		}			
	}
	
	override getDataType(QualifiedName name) {
		return dataTypes.get(name.name);
	}
	
	override isKnownType(QualifiedName name) {
		return dataTypes.containsKey(name.name);
	}
	
	override reportError(String message, EObject source, EStructuralFeature feature) {
		// Left intentionally empty - no need to report type errors during generation, document should be valid at this point
	}
	
}
