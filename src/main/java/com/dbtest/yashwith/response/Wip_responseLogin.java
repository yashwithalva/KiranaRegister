package com.dbtest.yashwith.response;

public class Wip_responseLogin {
    private String role;
    private String id;
    private String name;
    private String country;

    public Wip_responseLogin() {
    }

    public Wip_responseLogin(String role, String id, String name, String country){
        this.role = role;
        this.id = id;
        this.name = name;
        this.country = country;
    }
    public String getRole() {
        return this.role;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Wip_responseLogin)) return false;
        final Wip_responseLogin other = (Wip_responseLogin) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$role = this.getRole();
        final Object other$role = other.getRole();
        if (this$role == null ? other$role != null : !this$role.equals(other$role)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Wip_responseLogin;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $role = this.getRole();
        result = result * PRIME + ($role == null ? 43 : $role.hashCode());
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "Wip_responseLogin(role=" + this.getRole() + ", id=" + this.getId() + ", name=" + this.getName() + ")";
    }
}
