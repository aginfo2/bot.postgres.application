package br.com.simnetwork.model.service.dialog;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardDownRightHandler;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import br.com.simnetwork.model.entity.acesso.Acesso;
import br.com.simnetwork.model.entity.basico.usuario.Usuario;
import br.com.simnetwork.model.entity.basico.validacao.Validacao;
import br.com.simnetwork.model.entity.framework.App;
import br.com.simnetwork.view.DialogTypeFinish;

@Service("stepConfirmation")
public class DialogStepConfimation implements DialogStep {

	public StringBuilder mensagemBot = new StringBuilder();

	@Override
	public Object action(Usuario usuario, String mensagemUsuario, DialogTypeFinish currentDialogTypeFinish,
			JSONObject complements) {

		try {

			if (currentDialogTypeFinish.equals(DialogTypeFinish.INICIOSTEP)
					|| currentDialogTypeFinish.equals(DialogTypeFinish.CONTEUDOINVALIDO)) {
				
				mensagemBot.append("Deseja confirmar a gravação dos seguintes dados?\n");
				
				for (String complementKey : complements.keySet()) {
					mensagemBot.append(complementKey+" : "+complements.getString(complementKey));
				}
				
				List<String> keyboard = new LinkedList<>();
				keyboard.add("Sim");
				keyboard.add("Não");
				
				bot.prepareKeyboard(keyboard);
				
				bot.sendMessage(usuario, mensagemBot.toString());
				return DialogTypeFinish.AGUARDANDODADO;
			}

			if (currentDialogTypeFinish.equals(DialogTypeFinish.AGUARDANDODADO)) {
				
				if(mensagemUsuario.equals("Sim")) {
					return DialogTypeFinish.CONFIRMADO;
				}else {
					return DialogTypeFinish.CANCELADO;
				}
				
			}

			return currentDialogTypeFinish.ERRO;

		} catch (Exception e) {
			Acesso access = App.getCon().getBean("access", Acesso.class);
			if (usuario.getApelido() != null) {
				bot.sendMessage(access.getAdminTelegram(),
						"Ocorreu o seguinte erro para o usuário: " + usuario.getApelido() + " \n" + e.getMessage());
			} else {
				bot.sendMessage(access.getAdminTelegram(),
						"Ocorreu o seguinte erro (não foi possivel saber o usuário): " + e.getMessage());
			}
			return DialogTypeFinish.ERRO;

		}

	}

}