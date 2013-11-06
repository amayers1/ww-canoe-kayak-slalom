package com.tcay.samples;

import javax.swing.table.AbstractTableModel;

public class ExampleTableModel extends AbstractTableModel
{
	private final String[] columnNames = { "Month", "Income" };
		
	final Object[][] data = {
		{"January",   new Integer(150) },
		{"February",  new Integer(500) },
		{"March",     new Integer(54)  },
		{"April",     new Integer(-50) },
		{"May",       new Integer(52)  },
		{"June",      new Integer(74)  },
		{"July",      new Integer(-25) },
		{"August",    new Integer(62)  },
		{"September", new Integer(15)  },
		{"October",   new Integer(-5)  },
		{"November",  new Integer(5)   },
		{"December",  new Integer(59)  } 
	};

public ExampleTableModel() {}
public Class getColumnClass( int column ) 
{
	return getValueAt(0, column).getClass();
}
public int getColumnCount() 
{
	return columnNames.length;
}
public String getColumnName( int column ) 
{
	return columnNames[column];
}
public int getRowCount() 
{
	return data.length;
}
public Object getValueAt( int row, int column ) 
{
	return data[row][column];
}
}
