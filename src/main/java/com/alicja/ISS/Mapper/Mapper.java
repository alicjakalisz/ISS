package com.alicja.ISS.Mapper;

public interface Mapper<E,D> {

   public E fromDtoToEntity(D dto);
   public  D fromEntityToDto (E entity);

}
