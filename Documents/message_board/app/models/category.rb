# Category
class Category < ActiveRecord::Base
  belongs_to :internet_forum
  has_many :forums

  validates :description, length: {
    minimum: 10,
    maximum: 80
  }

  validates :name, presence: true
  validates :description, presence: true

  def add_forum(forum, user)
    forums.push(forum) if user.privilege == 'Administrator'
  end

  def remove_forum(forum_id, user)
    forums.delete(forum_id) if user.privilege == 'Administrator'
  end
end
