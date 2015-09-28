///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Controllers;
//
//import javax.faces.bean.ApplicationScoped;
//import javax.faces.bean.RequestScoped;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
///**
// *
// * @author Willians
// */
//@ApplicationScoped
//public class EntityManagerProducer {
//
//	private EntityManagerFactory factory;
//	
//	public EntityManagerProducer() {
//		factory = Persistence.createEntityManagerFactory("AgendaPU");
//	}
//	
//	@Produces @RequestScoped
//	public EntityManager createEntityManager() {
//		return factory.createEntityManager();
//	}
//	
//	public void closeEntityManager(@Disposes EntityManager manager) {
//		manager.close();
//	}
//
//}
