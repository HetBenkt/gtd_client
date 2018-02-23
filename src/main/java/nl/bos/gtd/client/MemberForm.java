package nl.bos.gtd.client;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import nl.bos.gtd.server.entities.Member;

class MemberForm extends FormLayout {
    private final VaadinUI vaadinUI;
    private final TextField firstName;
    private final Button btnDelete;
    private Member member;
    private final Binder<Member> binder = new Binder<>(Member.class);

    public MemberForm(VaadinUI vaadinUI) {
        this.vaadinUI = vaadinUI;
        this.setSizeUndefined();

        firstName = new TextField();
        firstName.setPlaceholder("First name");

        TextField lastName = new TextField();
        lastName.setPlaceholder("Last name");

        TextField email = new TextField();
        email.setPlaceholder("E-mail");

        TextField nickName = new TextField();
        nickName.setPlaceholder("Nickname");

        PasswordField password = new PasswordField();
        password.setPlaceholder("Password");

        Button btnSave = new Button("Save");
        btnSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnSave.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        btnSave.addClickListener(clickEvent -> save());

        btnDelete = new Button("Delete");
        btnDelete.setStyleName(ValoTheme.BUTTON_DANGER);
        btnDelete.addClickListener(clickEvent -> delete());

        HorizontalLayout buttons = new HorizontalLayout(btnSave, btnDelete);
        this.addComponents(firstName, lastName, email, nickName, password, buttons);

        binder.bindInstanceFields(this);
    }

    public void setMember(Member member) {
        this.member = member;
        binder.setBean(member);
        if(member.getId() == null)
            btnDelete.setVisible(false);
        else
            btnDelete.setVisible(true);
        this.setVisible(true);
        firstName.selectAll();
        vaadinUI.getFilterText().clear();
    }

    private void delete() {
        vaadinUI.getMemberRepo().delete(member);
        vaadinUI.updateGrid();
        setVisible(false);
    }

    private void save() {
        vaadinUI.getMemberRepo().save(member);
        vaadinUI.updateGrid();
        setVisible(false);
    }
}
