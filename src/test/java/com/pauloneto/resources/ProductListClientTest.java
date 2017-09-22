package com.pauloneto.resources;

public class ProductListClientTest
{

   public static void main(String[] args)
   {
      list("http://localhost:8080/pauloneto/products");

   }

   public static void list(String uri)
   {
//      Client client = ClientBuilder.newClient();
//      Builder request = client.target(uri)
//            .request().accept(MediaType.APPLICATION_JSON);
//      Response response = request.buildGet().invoke();

//      System.out.println("List of products " + Arrays.toString(response.readEntity(Product[].class)));
   }
}
