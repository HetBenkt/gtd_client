package nl.bos.gtd.client;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
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
    private final Grid<Member> grid;
    private final IMemberRepository memberRepo;


    @Autowired
    public VaadinUI(IMemberRepository memberRepo) {
        this.memberRepo = memberRepo;
        grid = new Grid<>(Member.class);

    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(grid);
        Iterable<Member> members = memberRepo.findAll();
        for (Member member : members) {
            log.info(member.getEmail());
        }

        List<Member> memberList = new ArrayList<>();
        memberRepo.findAll().forEach(memberList::add);
        grid.setItems(memberList);
    }
}
