package autenticacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/cadAutenticacao")
public class CadAutenticacaoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CadAutenticacaoServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		// Gerando o JSON para enviar ao webservice
		JSONObject json = new JSONObject();
		json.put("deTokenEndpoint", request.getParameter("deTokenEndpoint"));
		json.put("deClientSecret", request.getParameter("deClientSecret"));
		json.put("deToken", request.getParameter("deToken"));
		json.put("idUsuario", request.getParameter("idUsuario"));
		json.put("idGrantType", Integer.parseInt(request.getParameter("idGrantType")));
		// Definindo o endpoint (URL) do web service
		URL url = new URL("http://localhost/seg/cadautenticacao.php");
		// Criando o objeto para conexão HTTP
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Configurando a conexão
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Accept", "application/json");
		conn.setDoOutput(true);
		try {
			// Enviando o json gerado pelo request
			OutputStream os = conn.getOutputStream();
			byte[] input = json.toString().getBytes("utf-8");
			os.write(input, 0, input.length);
			// recebendo a resposta (response) do web service
			StringBuilder responseContent = new StringBuilder();
			InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				responseContent.append(line.trim());
			}
			// Enviando response para o cliente http
			response.setContentType("application/json");
			response.getWriter().write(responseContent.toString());
		} catch (Error e) {
			System.out.println(e.getMessage());
		}
	}
}
