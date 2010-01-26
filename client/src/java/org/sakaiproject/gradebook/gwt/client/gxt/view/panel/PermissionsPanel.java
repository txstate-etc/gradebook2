/**********************************************************************************
*
* $Id: GraderPermissionSettingsPanel.java 65687 2010-01-12 22:03:47Z jlrenfro@ucdavis.edu $
*
***********************************************************************************
*
* Copyright (c) 2008, 2009 The Regents of the University of California
*
* Licensed under the
* Educational Community License, Version 2.0 (the "License"); you may
* not use this file except in compliance with the License. You may
* obtain a copy of the License at
* 
* http://www.osedu.org/licenses/ECL-2.0
* 
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS"
* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
* or implied. See the License for the specific language governing
* permissions and limitations under the License.
*
**********************************************************************************/

package org.sakaiproject.gradebook.gwt.client.gxt.view.panel;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.sakaiproject.gradebook.gwt.client.AppConstants;
import org.sakaiproject.gradebook.gwt.client.I18nConstants;
import org.sakaiproject.gradebook.gwt.client.RestBuilder;
import org.sakaiproject.gradebook.gwt.client.UrlArgsCallback;
import org.sakaiproject.gradebook.gwt.client.RestBuilder.Method;
import org.sakaiproject.gradebook.gwt.client.gxt.a11y.AriaButton;
import org.sakaiproject.gradebook.gwt.client.gxt.event.GradebookEvents;
import org.sakaiproject.gradebook.gwt.client.gxt.view.components.PermissionDeleteCellRenderer;
import org.sakaiproject.gradebook.gwt.client.model.CategoryType;
import org.sakaiproject.gradebook.gwt.client.model.EntityModelComparer;
import org.sakaiproject.gradebook.gwt.client.model.GradebookModel;
import org.sakaiproject.gradebook.gwt.client.model.GraderKey;
import org.sakaiproject.gradebook.gwt.client.model.ItemKey;
import org.sakaiproject.gradebook.gwt.client.model.PermissionKey;
import org.sakaiproject.gradebook.gwt.client.model.PermissionsModel;
import org.sakaiproject.gradebook.gwt.client.model.SectionKey;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;

public class PermissionsPanel extends ContentPanel {
	
	private final static int DELETE_ACTION_GRID_CELL_WITHOUT_CATEGORIES = 3;
	private final static int DELETE_ACTION_GRID_CELL_WITH_CATEGORIES = 4;
	
	private final static String CAN_VIEW_PERMISSION = "view";
	private final static String CAN_GRADE_PERMISSION = "grade";

	private VerticalPanel mainVerticalPanel = null;
	private HorizontalPanel inputHorizontalPanel = null;
	private HorizontalPanel userSelectionHorizontalPanel = null;
	private HorizontalPanel createPermissionHorizontalPanel = null;
	private Grid<ModelData> grid = null;
	private ListStore<ModelData> permissionEntryListStore = null;
	
	private ComboBox<ModelData> userComboBox = null;
	private ComboBox<PermissionType> permissionComboBox = null;
	private ComboBox<ModelData> categoryComboBox = null;
	private ComboBox<ModelData> sectionComboBox = null;
	
	private ListLoader<ListLoadResult<ModelData>> categoryLoader, permissionLoader, sectionsLoader, userLoader;
	
	public PermissionsPanel(I18nConstants i18n) {
		super();

		setFrame(true);
		setHeading(i18n.permissionsHeading());
		
		mainVerticalPanel = new VerticalPanel();
		mainVerticalPanel.setSpacing(5);
		mainVerticalPanel.setSize("100%", "100%");
		
		inputHorizontalPanel = new HorizontalPanel();
		
		userSelectionHorizontalPanel = new HorizontalPanel();
		userSelectionHorizontalPanel.setSpacing(5);
		
		createPermissionHorizontalPanel = new HorizontalPanel();
		createPermissionHorizontalPanel.setSpacing(5);
		
		// PERMISSIONS
		permissionLoader = RestBuilder.getDelayLoader(AppConstants.PERMISSIONS_ROOT, 
				EnumSet.allOf(PermissionKey.class), Method.GET, new UrlArgsCallback() {

					public String getUrlArg() {
						List<ModelData> users = userComboBox.getSelection();
						if (null != users && 1 == users.size()) {
							return (String)users.get(0).get(GraderKey.ID.name());
						}
						return null;
					}
					
				},
				GWT.getModuleBaseURL(), AppConstants.REST_FRAGMENT, 
				AppConstants.PERMISSIONS_FRAGMENT);
		
		permissionEntryListStore = new ListStore<ModelData>(permissionLoader);
		permissionEntryListStore.setModelComparer(new EntityModelComparer<ModelData>(PermissionKey.ID.name()));
		
		// LOADING DATA
		
		// USERS
		userLoader = RestBuilder.getDelayLoader(AppConstants.GRADER_ROOT, EnumSet.allOf(GraderKey.class), Method.GET, 
				GWT.getModuleBaseURL(), AppConstants.REST_FRAGMENT, AppConstants.GRADER_FRAGMENT);
		//userLoader.load();
		ListStore<ModelData> userListStore = new ListStore<ModelData>(userLoader);
		userListStore.setModelComparer(new EntityModelComparer<ModelData>(GraderKey.ID.name()));
		
		// PERMISSION TYPES
		List<PermissionType> permissionList = new ArrayList<PermissionType>();
		permissionList.add(new PermissionType(CAN_GRADE_PERMISSION));
		// GRBK-233 : For now, we only enable the can grade permission. The can view permission will 
		// be tracked int GRBK-245
		ListStore<PermissionType> permissionListStore = new ListStore<PermissionType>();
		permissionListStore.add(permissionList);		
		
		// CATEGORIES
		categoryLoader = RestBuilder.getDelayLoader(AppConstants.ITEMS_ROOT, EnumSet.allOf(ItemKey.class), Method.GET, 
				GWT.getModuleBaseURL(), AppConstants.REST_FRAGMENT, AppConstants.ITEMS_FRAGMENT);
		//categoryLoader.load();
		ListStore<ModelData> categoryListStore = new ListStore<ModelData>(categoryLoader);
		categoryListStore.setModelComparer(new EntityModelComparer<ModelData>(ItemKey.ID.name()));
		
		
		// SECTIONS
		sectionsLoader = 
			RestBuilder.getDelayLoader(AppConstants.SECTIONS_ROOT, EnumSet.allOf(SectionKey.class), Method.GET, 
					GWT.getModuleBaseURL(), AppConstants.REST_FRAGMENT, AppConstants.SECTION_FRAGMENT);
		sectionsLoader.setRemoteSort(true);
		ListStore<ModelData> sectionStore = new ListStore<ModelData>(sectionsLoader);
		sectionStore.setModelComparer(new EntityModelComparer<ModelData>(SectionKey.ID.name()));
		
		
		// Combo Boxes
		
		// Users
		userComboBox = new ComboBox<ModelData>();
		userComboBox.setEmptyText(i18n.usersEmptyText());
		userComboBox.setDisplayField(GraderKey.USER_DISPLAY_NAME.name());
		userComboBox.setWidth(150); 
		userComboBox.setStore(userListStore);
		userComboBox.setTypeAhead(true);
		userComboBox.setTriggerAction(TriggerAction.ALL);
		userComboBox.setEditable(false);
		userComboBox.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<ModelData> se) {
				
				// Reset UI components
				permissionComboBox.reset();
				sectionComboBox.reset();
				categoryComboBox.reset();
				permissionEntryListStore.removeAll();
				
				permissionLoader.load();
				//loadGrid();
				createPermissionHorizontalPanel.show();	
			}
		});
		
		// Permissions
		permissionComboBox = new ComboBox<PermissionType>();
		permissionComboBox.setEmptyText(i18n.permissionsEmptyText());
		permissionComboBox.setDisplayField("name");
		permissionComboBox.setWidth(100); 
		permissionComboBox.setStore(permissionListStore);
		permissionComboBox.setTypeAhead(true);
		permissionComboBox.setTriggerAction(TriggerAction.ALL);
		permissionComboBox.setEditable(false);

		// Categories
		categoryComboBox = new ComboBox<ModelData>();
		categoryComboBox.setEmptyText(i18n.categoriesEmptyText());
		categoryComboBox.setDisplayField(ItemKey.NAME.name());
		categoryComboBox.setWidth(150); 
		categoryComboBox.setStore(categoryListStore);
		categoryComboBox.setTypeAhead(true);
		categoryComboBox.setTriggerAction(TriggerAction.ALL);
		categoryComboBox.setEditable(false);


		// Sections
		sectionComboBox = new ComboBox<ModelData>();
		sectionComboBox.setEmptyText(i18n.sectionsEmptyText());
		sectionComboBox.setDisplayField(SectionKey.SECTION_NAME.name());
		sectionComboBox.setWidth(150); 
		sectionComboBox.setStore(sectionStore);
		sectionComboBox.setTypeAhead(true);
		sectionComboBox.setTriggerAction(TriggerAction.ALL);
		sectionComboBox.setEditable(false);
		
		
		// Add Button
		Button addButton = new Button(i18n.addButton());
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				
				PermissionsModel permissionEntryModel = new PermissionsModel();
				
				// Enforce selection
				List<ModelData> users = userComboBox.getSelection();
				if(null != users && 1 == users.size()) {
					permissionEntryModel.setUserDisplayName((String)users.get(0).get(GraderKey.USER_DISPLAY_NAME.name()));
					permissionEntryModel.setUserId((String)users.get(0).get(GraderKey.ID.name()));
				}
				else {
					MessageBox.alert("Warn", "Please select a user", null);
					return;
				}
				
				// Enforce selection
				List<PermissionType> permissions = permissionComboBox.getSelection();
				if(null != permissions && 1 == permissions.size()) {
					permissionEntryModel.setPermissionId(permissions.get(0).getName());
				}
				else {
					MessageBox.alert("Warn", "Please select a permission", null);
					return;
				}
				
				// If no category and or section is selected, we assume that all categories and or all sections are selected
				
				List<ModelData> categories = categoryComboBox.getSelection();
				if(null != categories && 1 == categories.size()) {
					ModelData categoryModel = categories.get(0);
					Long categoryId = categoryModel.get(ItemKey.CATEGORY_ID.name());
					permissionEntryModel.setCategoryId(categoryId);
					permissionEntryModel.setCategoryDisplayName((null == categoryId) ? "All" : (String)categoryModel.get(ItemKey.NAME.name()));
				}
				else {
					// Per old gradebook, it seems that if all categories are selected, the ID is set to null
					permissionEntryModel.setCategoryId(null);
					permissionEntryModel.setCategoryDisplayName("All");
				}
				
				List<ModelData> sections = sectionComboBox.getSelection();
				if(null != sections && 1 == sections.size()) {
					ModelData sectionModel = sections.get(0);
					String sectionId = (String)sectionModel.get(SectionKey.ID.name());
					permissionEntryModel.setSectionId(sectionId);
					permissionEntryModel.setSectionDisplayName((null == sectionId) ? "All" : (String)sectionModel.get(SectionKey.SECTION_NAME.name()));
				}
				else {
					// Per old gradebook, it seems that if all sections are selected, the ID is set to null
					permissionEntryModel.setSectionId(null);
					permissionEntryModel.setSectionDisplayName("All");
				}
				
				permissionEntryModel.setDeleteAction("Delete");
				
				Dispatcher.forwardEvent(GradebookEvents.CreatePermission.getEventType(), permissionEntryModel);
				
				// Before we actually add the permission, we check if it's a duplicate
				/*if(isDuplicate(permissionEntryModel)) {
					
					MessageBox.alert("WARN", "Selected permission already exists", null);
				}
				else {*/
					// RPC Create Call
					//create(permissionEntryModel);
				//}
			}
		});

		
		// GRID
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();  

		ColumnConfig column = new ColumnConfig();  
		column.setId(PermissionKey.USER_DISPLAY_NAME.name());  
		column.setHeader(i18n.userHeader());  
		column.setWidth(150);
		column.setMenuDisabled(true);
		column.setSortable(false);
		configs.add(column);  
		
		column = new ColumnConfig();  
		column.setId(PermissionKey.PERMISSION_ID.name());  
		column.setHeader(i18n.permissionHeader());  
		column.setWidth(100);
		column.setMenuDisabled(true);
		column.setSortable(false);
		configs.add(column);

		// We only show the categories if the GB is setup that way
		if(hasCategories()) {
			
			column = new ColumnConfig();
			column.setId(PermissionKey.CATEGORY_DISPLAY_NAME.name());
			column.setHeader(i18n.categoryHeader());
			column.setWidth(150);
			column.setMenuDisabled(true);
			column.setSortable(false);
			configs.add(column);
		}
		
		column = new ColumnConfig();  
		column.setId(PermissionKey.SECTION_DISPLAY_NAME.name());  
		column.setHeader(i18n.sectionHeader());  
		column.setWidth(150);
		column.setMenuDisabled(true);
		column.setSortable(false);
		configs.add(column);
		
		column = new ColumnConfig();  
		column.setId(PermissionKey.DELETE_ACTION.name());  
		column.setHeader(i18n.deleteHeader());  
		column.setWidth(100);
		column.setMenuDisabled(true);
		column.setSortable(false);
		column.setRenderer(new PermissionDeleteCellRenderer());
		configs.add(column);
		
		  
		ColumnModel permissionEntryColumnModel = new ColumnModel(configs);
		final CellSelectionModel<ModelData> cellSelectionModel = new CellSelectionModel<ModelData>();
		cellSelectionModel.setSelectionMode(SelectionMode.SINGLE);
		grid = new Grid<ModelData>(permissionEntryListStore, permissionEntryColumnModel);  
		grid.setBorders(true);
		grid.setSelectionModel(cellSelectionModel);
		grid.setAutoHeight(true);
		grid.addListener(Events.CellClick, new Listener<GridEvent>() {

			public void handleEvent(GridEvent gridEvent) {

				if(hasCategories()) {
					
					if(DELETE_ACTION_GRID_CELL_WITH_CATEGORIES == cellSelectionModel.getSelectCell().cell) {
						ModelData permissionEntryModel = cellSelectionModel.getSelectCell().model;
						Dispatcher.forwardEvent(GradebookEvents.DeletePermission.getEventType(), permissionEntryModel);
						/*Gradebook2RPCServiceAsync service = Registry.get("service");
						GradebookModel model = Registry.get(AppConstants.CURRENT);
						service.delete(model.getGradebookUid(), model.getGradebookId(), permissionEntryModel, EntityType.PERMISSION_ENTRY, SecureToken.get(), getDeletePermissionEntryAsyncCallback());
						*/
					}
				}
				else {
					
					if(DELETE_ACTION_GRID_CELL_WITHOUT_CATEGORIES == cellSelectionModel.getSelectCell().cell) {
						ModelData permissionEntryModel = cellSelectionModel.getSelectCell().model;
						Dispatcher.forwardEvent(GradebookEvents.DeletePermission.getEventType(), permissionEntryModel);
						/*Gradebook2RPCServiceAsync service = Registry.get("service");
						GradebookModel model = Registry.get(AppConstants.CURRENT);
						service.delete(model.getGradebookUid(), model.getGradebookId(), permissionEntryModel, EntityType.PERMISSION_ENTRY, SecureToken.get(), getDeletePermissionEntryAsyncCallback());
						*/
					}
				}
			}
		});

		inputHorizontalPanel.add(userSelectionHorizontalPanel);
		inputHorizontalPanel.add(createPermissionHorizontalPanel);
		
		userSelectionHorizontalPanel.add(userComboBox);
		createPermissionHorizontalPanel.add(new HTML("can"));
		createPermissionHorizontalPanel.add(permissionComboBox);
		if(hasCategories()) {
			createPermissionHorizontalPanel.add(categoryComboBox);
			createPermissionHorizontalPanel.add(new HTML("in"));
		}
		createPermissionHorizontalPanel.add(sectionComboBox);
		createPermissionHorizontalPanel.add(addButton);
		
		// Initially we hide the create permission panel
		createPermissionHorizontalPanel.hide();

		mainVerticalPanel.add(inputHorizontalPanel);
		mainVerticalPanel.add(grid);
		
		add(mainVerticalPanel);
		
		// Standard Close button
		Button button = new AriaButton(i18n.close(), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent be) {
				Dispatcher.forwardEvent(GradebookEvents.StopGraderPermissionSettings.getEventType());
			}
			
		});

		addButton(button);		
	}
	
	public void onPermissionCreated(ModelData model) {
		permissionLoader.load();
	}
	
	public void onPermissionDeleted(ModelData model) {
		permissionLoader.load();
	}
	
	@Override
	protected void onRender(Element parent, int pos) {	    
		super.onRender(parent, pos);
		categoryLoader.load();
		userLoader.load();
		sectionsLoader.load();
	}
	
	/*
	private AsyncCallback<PermissionEntryListModel> getPermissionEntryListAsyncCallback() {
		return new AsyncCallback<PermissionEntryListModel>() {

			public void onFailure(Throwable caught) {
				// FIXME: show message
			}

			public void onSuccess(PermissionEntryListModel result) {
				permissionEntryListStore.removeAll();
				List<PermissionEntryModel> permissionEntryModelList = result.getEntries();
				
				if(null != permissionEntryModelList && permissionEntryModelList.size() > 0) {
					permissionEntryListStore.add(permissionEntryModelList);
				}
			}
		};
	}*/
	/*
	private AsyncCallback<PermissionEntryModel> getCreatePermissionEntryAsyncCallback() {
		
		return new AsyncCallback<PermissionEntryModel>() {

			public void onFailure(Throwable caught) {
				// FIXME: show message
			}

			public void onSuccess(PermissionEntryModel result) {

				permissionEntryListStore.add(result);
			}
		};
	}
	
	private AsyncCallback<PermissionEntryModel> getDeletePermissionEntryAsyncCallback() {
		return new AsyncCallback<PermissionEntryModel>() {

			public void onFailure(Throwable caught) {
				// FIXME: show message
			}

			public void onSuccess(PermissionEntryModel result) {
				permissionEntryListStore.remove(result);
				loadGrid();
			}
		};
	}
	
	private void loadGrid() {
		
		List<ModelData> users = userComboBox.getSelection();
		if(null != users && 1 == users.size()) {

			String userId = (String)users.get(0).get(GraderKey.ID.name());
			Gradebook2RPCServiceAsync service = Registry.get("service");
			GradebookModel gbModel = Registry.get(AppConstants.CURRENT);
			service.get(gbModel.getGradebookUid(), gbModel.getGradebookId(), EntityType.PERMISSION_ENTRY, userId, Boolean.TRUE, SecureToken.get(), getPermissionEntryListAsyncCallback());
		}
	}
	
	private void create(PermissionEntryModel permissionEntryModel) {
		Gradebook2RPCServiceAsync service = Registry.get("service");
		GradebookModel model = Registry.get(AppConstants.CURRENT);
		service.create(model.getGradebookUid(), model.getGradebookId(), permissionEntryModel, EntityType.PERMISSION_ENTRY, SecureToken.get(), getCreatePermissionEntryAsyncCallback());
		
	}*/
			
	private boolean hasCategories() {
		GradebookModel gbModel = Registry.get(AppConstants.CURRENT);
		CategoryType categoryType = gbModel.getGradebookItemModel().getCategoryType();
		
		switch(categoryType) {
		case NO_CATEGORIES:
			return false;
		case SIMPLE_CATEGORIES:
		case WEIGHTED_CATEGORIES:
			return true;
		default:
			return false;
		}
	}
	
	/*
	private boolean isDuplicate(PermissionEntryModel newPermissionEntryModel) {
		
		List<ModelData> permissionEntryModels = permissionEntryListStore.getModels();
		
		for(ModelData permissionEntryModel : permissionEntryModels) {
			
			// Compare user
			if(!permissionEntryModel.getUserId().equals(newPermissionEntryModel.getUserId())) {
				continue;
			}
			
			// Compare permission
			if(!permissionEntryModel.getPermissionId().equals(newPermissionEntryModel.getPermissionId())) {
				continue;
			}
			
			// Compare sections
			if((null != permissionEntryModel.getSectionId() && null != newPermissionEntryModel.getSectionId()) &&
				(!permissionEntryModel.getSectionId().equals(newPermissionEntryModel.getSectionId()))) {
				continue;
			}
			
			if(null == permissionEntryModel.getSectionId() && null != newPermissionEntryModel.getSectionId()) {
				continue;
			}
			
			if(null != permissionEntryModel.getSectionId() && null == newPermissionEntryModel.getSectionId()) {
				continue;
			}
			
			// If the gradebook is setup with no categories, we are done
			if(!hasCategories()) {
				return true;
			}
			
			// At this point of the checking, the gradebook was setup with categories
			
			// Compare categories
			if((null != permissionEntryModel.getCategoryId() && null != newPermissionEntryModel.getCategoryId()) &&
			    (!permissionEntryModel.getCategoryId().equals(newPermissionEntryModel.getCategoryId()))) {
				continue;
			}
			
			if(null == permissionEntryModel.getCategoryId() && null != newPermissionEntryModel.getCategoryId()) {
				continue;
			}
			
			if(null != permissionEntryModel.getCategoryId() && null == newPermissionEntryModel.getCategoryId()) {
				continue;
			}
			
			return true;
		}
		
		return false;
	}
	*/
	
	private class PermissionType extends BaseModel {

		private static final long serialVersionUID = 1L;
		
		public PermissionType(String name) {
			super();
			setName(name);
		}
		
		public String getName() {
			return get("name");
		}

		public void setName(String name) {
			set("name", name);
		}
	}
}