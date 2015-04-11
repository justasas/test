# Forum
class Forum < ActiveRecord::Base
  belongs_to :category
  belongs_to :account
  has_many :topics

  validates :title, length: {
    minimum: 10,
    maximum: 80
  }

  # validates :account, presence: true
  validates :title, presence: true
  validates :description, presence: true

  def create_topic(topic)
    topics.push(topic)
  end

  def delete_topic(topic_id, user)
    topics.delete(topic_id) if user.privilege == 'Administrator' ||
      user.privilege == 'Moderator'
  end
end
