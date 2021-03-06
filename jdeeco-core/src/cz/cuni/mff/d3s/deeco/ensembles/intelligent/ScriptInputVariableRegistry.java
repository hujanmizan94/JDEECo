package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Contains all input variables for a MiniZinc script. It also provide methods for adding more
 * variables to the input, as well as for acquiring the input variables and their values.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class ScriptInputVariableRegistry {

	static class Entry {
		String name;
		String value;
		
		public Entry(String name2, String value2) {
			name = name2;
			value = value2;
		}
		
		@Override
		public String toString() {
			return String.format("%s=%s", name, value);
		}
	}
	
	private List<Entry> inputVariables;
	
	/**
	 * These are the only primitive types that are supported. One can also use 1D or 2D arrays of these types,
	 * or sets of integers (sets of other primitive types are not allowed).
	 */
	// supported primitive types (for others -> exception)
	// for each there needs to be an overload of the addVariable function
	// supported are also arrays and sets
	public static final Set<Class<?>> supportedPrimitiveTypes
			= new HashSet<Class<?>>(Arrays.asList(Boolean.class, Integer.class, Float.class, ScriptIdentifier.class));

	/**
	 * Just for testing purposes. Use the parameterless constructor instead
	 * @param entryList The underlying list (or its mock).
	 */
	public ScriptInputVariableRegistry(List<Entry> entryList) {
		this.inputVariables = entryList;
	}
	
	public ScriptInputVariableRegistry() {
		this(new ArrayList<Entry>());
	}
	
	/**
	 * Returns a list of all variables in the registry.
	 * @return List of variables in the registry.
	 */
	public List<Entry> getInputVariables() {
		return inputVariables;
	}
	
	private String valueToStr(Object value) throws UnsupportedVariableTypeException {
		if (!supportedPrimitiveTypes.contains(value.getClass())) {
			throw new UnsupportedVariableTypeException(value.getClass());
		}
		
		return String.format(Locale.US, value.toString());
	}
	
	private void addVariable(String name, Object value) {
		try {
			inputVariables.add(new Entry(name, valueToStr(value)));
		} catch (UnsupportedVariableTypeException e) {
			assert false; // should not happen, called only from other addVariable mutations which should be safe
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds new variable and assigns it a boolean value.
	 * @param name Name of the variable (must correspond to a variable in the script).
	 * @param value A value.
	 */
	public void addVariable(String name, Boolean value) {
		addVariable(name, (Object) value);
	}
	
	/**
	 * Adds new variable and assigns it an integer value.
	 * @param name Name of the variable (must correspond to a variable in the script).
	 * @param value A value.
	 */
	public void addVariable(String name, Integer value) {
		addVariable(name, (Object) value);
	}

	/**
	 * Adds new variable and assigns it a float value.
	 * @param name Name of the variable (must correspond to a variable in the script).
	 * @param value A value.
	 */
	public void addVariable(String name, Float value) {
		addVariable(name, (Object) value);
	}

	/**
	 * Adds new variable and assigns it a constant identifier value.
	 * @param name Name of the variable (must correspond to a variable in the script).
	 * @param value A constant name (must be defined in the script).
	 */
	public void addVariable(String name, ScriptIdentifier value) {
		addVariable(name, (Object) value);
	}
	
	/**
	 * Adds new variable and assigns it a constant identifier value.
	 * @param name Name of the variable (must correspond to a variable in the script).
	 * @param value A constant name (must be defined in the script).
	 */
	public void addIdentifierVariable(String name, String identifier) {
		addVariable(name, new ScriptIdentifier(identifier));
	}
	
	/**
	 * Adds new array and assigns it inner values.
	 * @param name Name of the variable (must correspond to a variable in the script).
	 * @param value Array of values (must be homogeneous, only supported types can be used).
	 */
	public void addVariable(String name, Object[] value) throws UnsupportedVariableTypeException, HeterogeneousArrayException {
		String[] valuesStr = new String[value.length];
		Class<?> innerType = null;
		
		for (int i = 0; i < value.length; i++) {
			if (i == 0) {
				innerType = value[i].getClass(); 
			} else if (!innerType.isAssignableFrom(value[i].getClass())) {
				throw new HeterogeneousArrayException("The supplied array is not valid. All members of the array must be of the same type.");
			}
			
			valuesStr[i] = valueToStr(value[i]);
		}
		
		String result = String.format("[%s]", String.join(",", valuesStr));
		inputVariables.add(new Entry(name, result));
	}
	
	/**
	 * Adds new 2D array and assigns it inner values.
	 * @param name Name of the variable (must correspond to a variable in the script).
	 * @param value 2D array of values (must be homogeneous, only supported types can be used).
	 */	
	public void addVariable(String name, Object[][] value) throws UnsupportedVariableTypeException, HeterogeneousArrayException {
		StringBuilder result = new StringBuilder("[");
		Class<?> innerType = null;
		int innerLength = 0;
		
		for (int i = 0; i < value.length; i++) {
			if (i == 0) {
				innerLength = value[i].length;
			} else if (innerLength != value[i].length) {
				throw new HeterogeneousArrayException("The supplied array is not valid. When using 2D arrays, the length of all inner items must be equal. Symbolically, the number of 'columns' must be the same for each 'row'.");
			}
			String[] valuesStr = new String[value[i].length];
			
			for (int j = 0; j < value[i].length; j++) {
				if (i == 0 && j == 0) {
					innerType = value[i][j].getClass(); 
				} else if (!innerType.isAssignableFrom(value[i][j].getClass())) {
					throw new HeterogeneousArrayException("The supplied array is not valid. All members of the array must be of the same type.");
				}
				
				valuesStr[j] = valueToStr(value[i][j]);
			}
			
			result.append(String.format("|%s", String.join(",", valuesStr)));
		}
		
		if (innerLength > 0) {
			result.append("|");
		}
		
		result.append("]");
		inputVariables.add(new Entry(name, result.toString()));
	}
	
	/**
	 * Adds new set and assigns it.
	 * @param name Name of the variable (must correspond to a variable in the script).
	 * @param value Set of integer values (other primitive types are not allowed).
	 */	
	public void addVariable(String name, Set<Integer> value) {
		List<String> valuesStr = new ArrayList<>();
		
		for (Integer item : value) {			
			try {
				valuesStr.add(valueToStr(item));
			} catch (UnsupportedVariableTypeException e) {
				assert false; // should not happen, items are of type Integer, which is supported.
				e.printStackTrace();
			}
		}
		
		String result = String.format("{%s}", String.join(",", valuesStr));
		inputVariables.add(new Entry(name, result));
	}

}
