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
package pl.karol202.evolution.entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Stream;

class EntitiesList extends ArrayList<EntitiesList.SelectableEntity>
{
	class SelectableEntity
	{
		private Entity entity;
		private boolean selected;
		
		SelectableEntity(Entity entity, boolean selected)
		{
			this.entity = entity;
			this.selected = selected;
		}
		
		void update()
		{
			entity.update();
		}
		
		Entity getEntity()
		{
			return entity;
		}
		
		boolean isSelected()
		{
			return selected;
		}
		
		void setSelected(boolean selected)
		{
			this.selected = selected;
		}
	}
	
	void addEntity(Entity entity, boolean selected)
	{
		if(containsEntity(entity)) throw new IllegalArgumentException("Entity is already in list.");
		add(new SelectableEntity(entity, selected));
	}
	
	boolean containsEntity(Entity entity)
	{
		return stream().filter(se -> se.getEntity() == entity).count() > 0;
	}
	
	int indexOfEntity(Entity entity)
	{
		return stream().filter(se -> se.getEntity() == entity).map(SelectableEntity::getEntity).mapToInt(this::indexOf)
					   .findAny().orElse(-1);
	}
	
	Entity getEntityById(int id)
	{
		if(id == -1) return null;
		return get(id).getEntity();
	}
	
	void removeEntity(Entity entity)
	{
		if(!containsEntity(entity)) throw new IllegalArgumentException("Unknown entity: " + entity);
		removeIf(se -> se.getEntity() == entity);
	}
	
	Stream<Entity> entitiesStream()
	{
		return stream().map(SelectableEntity::getEntity);
	}
	
	Stream<Entity> selectedEntitiesStream()
	{
		return stream().filter(SelectableEntity::isSelected).map(SelectableEntity::getEntity);
	}
	
	void updateAll()
	{
		forEach(SelectableEntity::update);
	}
	
	void selectOnlyEntity(Entity entity)
	{
		if(!containsEntity(entity)) throw new IllegalArgumentException("Unknown entity: " + entity);
		forEach(se -> se.setSelected(se.getEntity() == entity));
	}
	
	void selectEntity(Entity entity)
	{
		setEntitySelection(entity, true);
	}
	
	void deselectEntity(Entity entity)
	{
		setEntitySelection(entity, false);
	}
	
	private void setEntitySelection(Entity entity, boolean selected)
	{
		if(!containsEntity(entity)) throw new IllegalArgumentException("Unknown entity: " + entity);
		stream().filter(se -> se.getEntity() == entity).forEach(se -> se.setSelected(selected));
	}
	
	void selectEntitiesInRect(Rectangle rect)
	{
		setSelectionForEntitiesInRect(rect, true);
	}
	
	void deselectEntitiesInRect(Rectangle rect)
	{
		setSelectionForEntitiesInRect(rect, false);
	}
	
	private void setSelectionForEntitiesInRect(Rectangle rect, boolean selected)
	{
		stream().filter(se -> {
			Entity entity = se.getEntity();
			return entity.getX() > rect.getMinX() && entity.getX() < rect.getMaxX() &&
				   entity.getY() > rect.getMinY() && entity.getY() < rect.getMaxY();
		}).forEach(se -> se.setSelected(selected));
	}
	
	void selectNothing()
	{
		forEach(se -> se.setSelected(false));
	}
	
	void selectAll()
	{
		forEach(se -> se.setSelected(true));
	}
	
	boolean isEntitySelected(Entity entity)
	{
		return stream().filter(se -> se.getEntity() == entity).findAny()
					   .orElseThrow(() -> new IllegalArgumentException("Unknown entity: " + entity)).isSelected();
	}
}