package org.appfuse.tutorial.webapp.pages;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.appfuse.service.GenericManager;
import org.appfuse.tutorial.model.Person;
import org.junit.Test;
import org.springframework.web.context.support.StaticWebApplicationContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PersonFormTest extends BasePageTest {

    private GenericManager<Person, Long> personManager;

    @Override
    protected void initSpringBeans(StaticWebApplicationContext context) {
        super.initSpringBeans(context);
        personManager = mock(GenericManager.class);
        context.getBeanFactory().registerSingleton("personManager", personManager);
        PersonForm personForm = new PersonForm();
        tester.startPage(personForm);
    }

    @Test
    public void shouldDisplayErrorMessagesOnAddEmptyPerson() {
        // given
        tester.assertRenderedPage(PersonForm.class);
        tester.assertNoErrorMessage();
        tester.assertComponent("person-form", Form.class);

        // when
        tester.submitForm("person-form");

        // then
        tester.assertErrorMessages("'First Name' is required.", "'Last Name' is required.");
    }

    @Test
    public void shouldSavePersonAndRedirectToPersonListOnSubmitPersonWithRequiredData() {
        // given
        tester.assertRenderedPage(PersonForm.class);
        tester.assertNoErrorMessage();
        FormTester formTester = tester.newFormTester("person-form");
        Person person = createTestPerson();

        // when
        fillFormWithValuesFromPerson(formTester, person);
        formTester.submit("save");

        // then
        verify(personManager).save(person);
        tester.assertRenderedPage(PersonList.class);
        tester.assertNoErrorMessage();
        tester.assertInfoMessages("Person has been added successfully.");
    }

    private Person createTestPerson() {
        Person person = new Person();
        person.setFirstName("Joe");
        person.setLastName("Smith");
        return person;
    }

    private void fillFormWithValuesFromPerson(FormTester formTester, Person person) {
        formTester.setValue("firstName", person.getFirstName());
        formTester.setValue("lastName", person.getLastName());
    }
}
