package org.batfish.datamodel.routing_policy.expr;

import com.fasterxml.jackson.annotation.JsonCreator;

public class VarCommunitySetElemHalf implements CommunitySetElemHalfExpr {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private String _var;

   @JsonCreator
   public VarCommunitySetElemHalf() {
   }

   public VarCommunitySetElemHalf(String var) {
      _var = var;
   }

   public String getVar() {
      return _var;
   }

   public void setVar(String var) {
      _var = var;
   }

}
