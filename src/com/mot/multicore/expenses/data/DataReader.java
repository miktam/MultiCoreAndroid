package com.mot.multicore.expenses.data;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mot.multicore.expenses.OperationEntry;
import com.mot.multicore.expenses.OperationType;

import android.text.Spanned;

/**
 * Reads data from sent file
 */
public interface DataReader {
	
	Map<String, Spanned> readData(boolean allowCache) throws IOException;
	
	Set<String> getFiles();
	List<OperationEntry> getOperations(String file);
	List<String> getOperationsFromFileByType(String file, OperationType type);
	List<String> getOperationsStringByIndex(Integer index, OperationType type);
	List<String> getOperationsFromStringByType(String source, OperationType type);
	List<OperationEntry> getOperationsByIndex(Integer index, OperationType op);
}
