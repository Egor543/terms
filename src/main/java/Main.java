import model.Term;
import repository.TermRepository;
import view.TermPresenter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TermRepository termRepository = new TermRepository();
        TermPresenter termPresenter = new TermPresenter(termRepository, new Scanner(System.in));
        termPresenter.askForActivities();
    }
}
