/*
  Copyright 2017 karol-202
 
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
package pl.karol202.evolution.ui.entity;

import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.genes.Gene;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class GenotypeTableModel extends AbstractTableModel
{
	private Genotype genotype;
	private String filter;
	private List<Gene> filteredProperties;
	
	public GenotypeTableModel()
	{
		filter = "";
		filteredProperties = new ArrayList<>();
	}
	
	@Override
	public int getRowCount()
	{
		return filteredProperties.size();
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
		return filteredProperties.get(row);
	}
	
	private String getGeneName(Gene gene)
	{
		return String.format("%s.%d", gene.getType().name(), gene.getLevel());
	}
	
	public void setEntity(Entity entity)
	{
		if(entity != null) genotype = entity.getGenotype();
		else genotype = null;
		filterAllGenes();
		fireTableDataChanged();
	}
	
	public void setFilter(String filter)
	{
		this.filter = filter;
		filterAllGenes();
		fireTableDataChanged();
	}
	
	private void filterAllGenes()
	{
		filteredProperties.clear();
		if(genotype == null) return;
		for(GeneType type : GeneType.values())
			for(int level = 0; level < type.getLevels(); level++)
				filterGene(type, level);
	}
	
	private void filterGene(GeneType type, int level)
	{
		Gene gene = genotype.getGeneOfTypeAndLevel(type, level);
		String geneName = getGeneName(gene);
		if(geneName.contains(filter) || filter.isEmpty()) filteredProperties.add(gene);
	}
}