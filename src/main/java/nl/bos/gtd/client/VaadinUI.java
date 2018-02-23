package nl.bos.gtd.client;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.Getter;
import lombok.extern.java.Log;
import nl.bos.gtd.server.entities.Member;
import nl.bos.gtd.server.repositories.IMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@SpringUI
@Theme("valo")
@Log
public class VaadinUI extends UI {
    @Getter
    private final IMemberRepository memberRepo;
    private final Grid<Member> grid;
    @Getter
    private TextField filterText;
    private final Button btnClearFilter;
    private final Button btnAddCustomer;
    private final MemberForm form;

    @Autowired
    public VaadinUI(IMemberRepository memberRepo) {
        this.grid = new Grid<>(Member.class);
        this.filterText = new TextField();
        this.btnClearFilter = new Button("Clear the current filter");
        this.btnAddCustomer = new Button("Add new customer");
        this.form= new MemberForm(this);
        this.form.setVisible(false);
        this.memberRepo = memberRepo;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        main.setExpandRatio(grid, 1);

        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent.getValue() == null) {
                form.setVisible(true);
            } else {
                form.setMember(valueChangeEvent.getValue());
            }
        });

        CssLayout filtering = new CssLayout();
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filtering.addComponents(filterText, btnClearFilter);

        HorizontalLayout toolbar = new HorizontalLayout(filtering, btnAddCustomer);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(toolbar, main);

        btnClearFilter.addClickListener(clickEvent -> filterText.clear());
        btnAddCustomer.addClickListener(clickEvent -> {
            grid.asSingleSelect().clear();
            form.setMember(new Member());
        });

        handleFilterText();
        updateGrid();

        setContent(layout);
    }

    private void handleFilterText() {
        filterText.setPlaceholder("Filter by name...");
        filterText.addValueChangeListener(changeEvent -> updateGrid());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
    }

    void updateGrid() {
        List<Member> memberList = new ArrayList<>();

        if(filterText.getValue().equals("")) {
            Iterable<Member> members = memberRepo.findAll();
            for (Member member : members) {
                memberList.add(member);
            }
        } else {
            memberList.addAll(memberRepo.findFirst10ByLastName(filterText.getValue()));
        }

        grid.setItems(memberList);
    }
}
