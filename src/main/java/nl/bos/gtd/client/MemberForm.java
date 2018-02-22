package nl.bos.gtd.client;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import lombok.Setter;
import nl.bos.gtd.server.entities.Member;
import nl.bos.gtd.server.repositories.IMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class MemberForm extends FormLayout {
    @Autowired
    private IMemberRepository memberRepo;
    private final VaadinUI vaadinUI;
    private TextField firstName, lastName, email;
    private Button btnSave, btnDelete;
    private Member member;
    private final Binder<Member> binder = new Binder<>(Member.class);

    public MemberForm(VaadinUI vaadinUI) {
        this.vaadinUI = vaadinUI;
        this.setSizeUndefined();

        firstName = new TextField();
        firstName.setPlaceholder("First name");

        lastName = new TextField();
        lastName.setPlaceholder("Last name");

        email = new TextField();
        email.setPlaceholder("E-mail");

        btnSave = new Button("Save");
        btnSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnSave.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        btnSave.addClickListener(clickEvent -> save());

        btnDelete = new Button("Delete");
        btnDelete.addClickListener(clickEvent -> delete());

        HorizontalLayout buttons = new HorizontalLayout(btnSave, btnDelete);
        this.addComponents(firstName, lastName, email, buttons);

        binder.bindInstanceFields(this);
    }

    public void setMember(Member member) {
        this.member = member;
        binder.setBean(member);
        btnDelete.setVisible(false);
        this.setVisible(true);
        firstName.selectAll();
    }

    private void delete() {
        memberRepo.delete(member);
        vaadinUI.updateGrid();
        setVisible(false);
    }

    private void save() {
        memberRepo.save(member);
        vaadinUI.updateGrid();
        setVisible(false);
    }
}
