<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
	<head>
		<%@include file="base-head.jsp"%>
	</head>
	
	<body>
		<%@include file="nav-menu.jsp"%>
		
		<div id="container" class="container-fluid">
			<h3 class="page-header">Adicionar Produto</h3>

			<form action="${pageContext.request.contextPath}/produto/${action}" method="POST">
				<input type="hidden" value="${produto.getId()}" name="produtoId">
				<div class="row">
					<div class="form-group col-md-4">
						<label for="content">Nome do Produto</label>
						<input type="text" class="form-control" id="name" name="name" 
							   autofocus="autofocus" placeholder="Nome do produto" 
							   required oninvalid="this.setCustomValidity('Por favor, informe o nome do produto.')"
							   oninput="setCustomValidity('')"
							   value="${produto.getName()}">
					</div>
					
					<div class="form-group col-md-4">
						<label for="company">Empresa</label>
						<select id="company" class="form-control selectpicker" name="company" 
							    required oninvalid="this.setCustomValidity('Por favor, informe a empresa.')"
							    oninput="setCustomValidity('')">
						  <option value="" disabled ${not empty produto ? "" : "selected"}>Selecione uma empresa</option>
						  <c:forEach var="company" items="${companys}">
						  	<option value="${company.getId()}"  ${produto.getCompany().getId() == company.getId() ? "selected" : ""}>
						  		${company.getName()}
						  	</option>	
						  </c:forEach>
						</select>
					</div>
					
				</div>
				<hr/>
				<div class="row">
					<div class="form-group col-md-6">
						<label for="content">Data de Fabricação</label>
						<input type="date" class="form-control" id="fabrica" name="fabrica" 
							   autofocus="autofocus" placeholder="Início da fabricação" 
							   required oninvalid="this.setCustomValidity('Por favor, informe a data de fabricação do produto.')"
							   oninput="setCustomValidity('')"
							   value="${produto.getFabrica()}">
					</div>
					
					<div class="form-group col-md-6">
						<label for="content">Data de Validade</label>
						<input type="date" class="form-control" id="validade" name="validade" 
							   autofocus="autofocus" placeholder="Saída da empresa"
							   value="${produto.getValidade()}">
					</div>
				</div>
				
				<hr />
				<div id="actions" class="row pull-right">
					<div class="col-md-12">
						<a href="${pageContext.request.contextPath}/produtos" class="btn btn-default">Cancelar</a>
						<button type="submit" 
								class="btn btn-primary">${not empty produto ? "Alterar Produto" : "Criar Produto"}
						</button>
					</div>
				</div>
			</form>
		</div>
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</body>
</html>
