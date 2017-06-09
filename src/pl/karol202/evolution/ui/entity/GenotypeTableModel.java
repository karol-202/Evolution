package pl.karol202.evolution.ui.entity;

import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.genes.Gene;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;

import javax.swing.table.AbstractTableModel;

public class GenotypeTableModel extends AbstractTableModel
{
	private Genotype genotype;
	
	@Override
	public int getRowCount()
	{
		return GeneType.getAllGenesCount();
	}
	
	@Override
	public int getColumnCount()
	{
		return 3;
	}
	
	@Override
	public Object getValueAt(int row, int column)
	{
		if(genotype == null) return null;
		Gene gene = getGeneAtRow(row);
		if(column == 0) return getGeneName(gene);
		else if(column == 1) return gene.getAlleleA();
		else if(column == 2) return gene.getAlleleB();
		else return null;
	}
	
	private Gene getGeneAtRow(int row)
	{
		return genotype.getGeneByIndex(row);
	}
	
	private String getGeneName(Gene gene)
	{
		return String.format("%s.%d", gene.getType().name(), gene.getLevel());
	}
	
	public void setEntity(Entity entity)
	{
		if(entity != null) genotype = entity.getGenotype();
		else genotype = null;
		fireTableDataChanged();
	}
}