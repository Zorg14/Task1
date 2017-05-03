package my.vaadin.application;

import java.awt.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

	private HotelService service =  HotelService.getInstance();
	private Grid<Hotel> grid = new Grid<>(Hotel.class);
	private TextField filterAddress = new TextField();
	private TextField filterName = new TextField();
	private HotelForum form = new HotelForum(this);
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        filterAddress.setPlaceholder("filter by address...");
        filterAddress.addValueChangeListener(e -> updateList());
        filterAddress.setValueChangeMode(ValueChangeMode.LAZY);
        
        filterName.setPlaceholder("filter by name...");
        filterName.addValueChangeListener(e -> updateList1());
        filterName.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button clearAddressFilter = new Button(VaadinIcons.CLOSE_CIRCLE);
        clearAddressFilter.setDescription("Clear address filter.");
        clearAddressFilter.addClickListener(e -> filterAddress.clear());
        
        Button clearNameFilter = new Button(VaadinIcons.CLOSE_CIRCLE_O);
        clearNameFilter.setDescription("Clear name filter.");
        clearNameFilter.addClickListener(e -> filterName.clear());
        
        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterAddress, clearAddressFilter);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        CssLayout filtering1 = new CssLayout();
        filtering1.addComponents(filterName, clearNameFilter);
        filtering1.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        Button addHotel = new Button("Add new hotel");
        addHotel.addClickListener(e -> {
        	grid.asSingleSelect().clear();
        	form.setHotel(new Hotel());
        });
        
        HorizontalLayout toolBar = new HorizontalLayout(filtering, filtering1, addHotel);
        
        grid.setColumns("name", "address" , "feedback");
        
        grid.addColumn(hotel ->
        "<a href='" + hotel.getUrl() + "' target='_top'>info</a>",
        new HtmlRenderer()).setCaption("link");
        
       
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);
        
        layout.addComponents(toolBar, main );
        
        
        
        updateList();
        updateList1();
                 
        setContent(layout);
        
        form.setVisible(false);
        
        grid.asSingleSelect().addValueChangeListener(event ->{
        	if(event.getValue() == null)
        	{
        		form.setVisible(false);
        	} else
        	{
        		form.setHotel(event.getValue());
        	}
        });
    }

	void updateList() 
	{
		java.util.List<Hotel> hotel = service.findAll(filterAddress.getValue());
        grid.setItems(hotel);
	}
	
	private void updateList1() 
	{
		java.util.List<Hotel> hotel1 = service.findAll(filterName.getValue());
		grid.setItems(hotel1);       
	}

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
