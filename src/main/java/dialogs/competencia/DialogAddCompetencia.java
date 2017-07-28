package dialogs.competencia;

import java.util.LinkedList;
import java.util.List;

import com.pengrad.telegrambot.TelegramBot;

import dialogs.basic.structure.Dialog;
import mvc.Model;
import objects.Competencia;
import objects.CompetenciaTipo;
import objects.CompetenciaUN;
import objects.Termo;
import objects.TermoTopico;
import objects.basic.Person;
import objects.basic.Route;

public class DialogAddCompetencia extends Dialog {

	public DialogAddCompetencia(TelegramBot bot, Person person, Route route, Model model, String message) {
		super(bot, person, route, model, message);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dialog action() {

		List<String> keyboardTipo = new LinkedList<>();
		for (CompetenciaTipo tipo : model.competenciaTipo.values()) {
			keyboardTipo.add(tipo.descricao);
		}

		List<String> keyboardUN = new LinkedList<>();
		for (CompetenciaUN un : model.competenciaUN.values()) {
			keyboardUN.add(un.descricao);
		}

		if (nextStep()) {

			answer.append("Selecione o tipo da nova competência");

			prepareKeyboard(keyboardTipo, 2);

			return finishStep();
		}

		if (nextStep()) {
			if (keyboardTipo.contains(message) || isValid("tipo")) {
				addComplementString("tipo");

				answer.append("Digite a descrição da nova competência");

				return finishStep();
			} else {
				return messageInvalid();
			}
		}

		if (nextStep()) {

			addComplementString("descricao");
			answer.append("Selecione a unidade de medida da nova competência");

			prepareKeyboard(keyboardUN, 2);

			return finishStep();
		}

		if (nextStep()) {
			if (keyboardUN.contains(message) || isValid("UN")) {
				addComplementString("UN");

				answer.append("Digite o valor unitário");

				return finishStep();
			} else {
				return messageInvalid();
			}
		}
		
		if(nextStep()) {
			addComplementString("valor");
			Double valor;
			try {
				valor = Double.parseDouble(getComplementString("valor"));
			} catch (Exception e) {
				return messageInvalid();
			}
			
			answer.append("Confirma os seguintes dados?\n\n");
			answer.append("Tipo: " + getComplementString("tipo") + "\n");
			answer.append("Descrição: " + getComplementString("descricao") + "\n");
			answer.append("Unidade de Medida: " + getComplementString("UN") + "\n");
			answer.append("Valor unitário: " + getComplementString("valor") + "\n");

			messageConfirmation();

			return finishStep();
		}

		if (nextStep()) {
			if (isConfirmated()) {
				model.competenciaTipo.locateCompetenciaTipoByString(getComplementString("tipo"));
				Competencia competencia = new Competencia(
						getComplementString("descricao"),
						Double.parseDouble(getComplementString("valor")),
						model.competenciaTipo.locateCompetenciaTipoByString(getComplementString("tipo")),
						model.competenciaUN.locateCompetenciaUNByString(getComplementString("UN"))
				);
				model.addCompetencia(competencia);
				return finishHim();
			} else {
				return finishHim();
			}
		}
		return null;
	}

}
