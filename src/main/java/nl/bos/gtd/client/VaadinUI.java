package nl.bos.gtd.client;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.java.Log;
import nl.bos.gtd.server.entities.Member;
import nl.bos.gtd.server.repositories.IMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.ws.handler.MessageContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@SpringUI
@Theme("valo")
@Log
public class VaadinUI extends UI {
    @Autowired
    private IMemberRepository memberRepo;
    private final Grid<Member> grid;
    private TextField filterText;
    private Button btnClearFilter;
    private MemberForm form;

    public VaadinUI(IMemberRepository memberRepo) {
        this.grid = new Grid<>(Member.class);
        this.filterText = new TextField();
        this.btnClearFilter = new Button("Clear the current filter");
        this.form= new MemberForm(this);

    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);
        VerticalLayout layout = new VerticalLayout();
        CssLayout filtering = new CssLayout();
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filtering.addComponents(filterText, btnClearFilter);

        layout.addComponents(filtering, main);

        initClearFilterBtn();
        handleFilterText();
        updateGrid();

        setContent(layout);
    }

    private void initClearFilterBtn() {
        btnClearFilter.addClickListener(clickEvent -> filterText.clear());
    }

    private void handleFilterText() {
        filterText.setPlaceholder("Filter by name...");
        filterText.addValueChangeListener(changeEvent -> updateGrid());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
    }

    protected void updateGrid() {
        Iterable<Member> members = memberRepo.findAll();
        for (Member member : members) {
            log.info(member.getEmail());
        }

        List<Member> memberList = new ArrayList<>();
        memberRepo.findFirst10ByLastName(filterText.getValue()).forEach(memberList::add);
        grid.setItems(memberList);
    }
}
