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
package pl.karol202.evolution.genes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Random;

import static pl.karol202.evolution.simulation.SimulationManager.getElement;
import static pl.karol202.evolution.simulation.SimulationManager.getIntAttribute;
import static pl.karol202.evolution.simulation.SimulationManager.setNumberAttribute;

public class GenesLoader
{
	private Document document;
	
	public Genotype parseGenotype(Element entityElement)
	{
		Element elementGenotype = getElement(entityElement, "genotype");
		
		Genotype genotype = new Genotype(new Random());
		
		NodeList genesNodes = elementGenotype.getChildNodes();
		for(int i = 0; i < genesNodes.getLength(); i++)
		{
			Element elementGene = (Element) genesNodes.item(i);
			genotype.setGene(parseGene(elementGene));
		}
		return genotype;
	}
	
	private Gene parseGene(Element geneElement)
	{
		GeneType type = GeneType.getTypeByName(geneElement.getAttribute("type"));
		int level = getIntAttribute(geneElement, "level");
		Allele alleleA = Allele.getAlleleByName(geneElement.getAttribute("alleleA"));
		Allele alleleB = Allele.getAlleleByName(geneElement.getAttribute("alleleB"));
		return new Gene(type, level, alleleA, alleleB);
	}
	
	public Element getGenotypeElement(Document document, Genotype genotype)
	{
		this.document = document;
		return createGenotypeElement(genotype);
	}
	
	private Element createGenotypeElement(Genotype genotype)
	{
		Element elementGenotype = document.createElement("genotype");
		processAllGenesInGenotype(genotype, elementGenotype);
		return elementGenotype;
	}
	
	private void processAllGenesInGenotype(Genotype genotype, Element genotypeElement)
	{
		for(GeneType type : GeneType.values())
			for(int level = 0; level < type.getLevels(); level++)
			{
				Gene gene = genotype.getGeneOfTypeAndLevel(type, level);
				genotypeElement.appendChild(createGeneElement(gene));
			}
	}
	
	private Element createGeneElement(Gene gene)
	{
		Element elementGene = document.createElement("gene");
		elementGene.setAttribute("type", gene.getType().name());
		setNumberAttribute(elementGene, "level", gene.getLevel());
		elementGene.setAttribute("alleleA", gene.getAlleleA().name());
		elementGene.setAttribute("alleleB", gene.getAlleleB().name());
		return elementGene;
	}
}