module ApplicationHelper
  def format_name(user)
    return "@" + user.username unless user.username.blank?
    return user.name unless user.name.blank?
    return user.email
  end

end
