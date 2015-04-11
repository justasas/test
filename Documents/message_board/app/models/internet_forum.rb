# InternetForum
class InternetForum < ActiveRecord::Base
  has_many :accounts
  has_many :categories

  validates :description, length: {
    minimum: 10,
    maximum: 80
  }

  validates :title, presence: true
  validates :description, presence: true

  def register_new_user(user)
    accounts.each { |account| return false if account.name == user.name }
    accounts.push user
  end

  def remove_user(user_id, user)
    if user.privilege == 'Administrator'
      accounts.delete(user_id)
      return
    else
      accounts.delete(user_id) if accounts.find(user_id).name == user.name
    end
  end

  def log_in(name, pass)
    found_account = nil
    accounts.each do |account|
      found_account = account if account.name == name
    end
    return 'user does not exist' if found_account.nil?
    return 'pasword is not correct' if found_account.pass != pass
    found_account
  end

  def add_category(category, user)
    categories.each { |cat| return false if category.name == cat.name }
    categories.push(category) if user.privilege.eql? 'Administrator'
  end

  def remove_category(category_id, user)
    categories.delete(category_id) if user.privilege == 'Administrator'
  end
end
