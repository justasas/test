# Topic
class Topic < ActiveRecord::Base
  belongs_to :account
  delegate :name, :to => :account, :prefix => true
  belongs_to :forum
  has_many :replies, dependent: :destroy
  has_many :ratings
  has_many :accounts, through: :ratings

  validates :title, length: {
    minimum: 10,
    maximum: 80
  }

  # validate :validate_ratings

  # def validate_ratings
  #   if !ratings.is_a?(Array) || ratings.detect{|r| !(1..5).include?(d)}
  #     errors.add(:ratings, :invalid)
  #   end
  # end

  validates :replies, presence: true
  validates :title, presence: true

  after_create :add_owner

  def add_owner
    update_attribute(:account, replies.first.account)
  end
  # def initialize(title)
  # @title = title
  # @replies = replies
  # @account = replies.first.account
  # @account.topics.push self
  # @users_who_rated = []
  # @ratings = []
  # end

  def add_reply(reply, *quotes)
    reply.replies = quotes
    replies.push reply
  end

  def remove_reply(user, reply)
    # index = self.replies.index(reply)
    user.privilege == 'Administrator' ? reply.destroy : false
  end

  def edit_reply(user, reply, text)
    # return false if user.replies.index(replies[id]).nil? && user.privilege !=
    #  'Administrator'
    return false if user.privilege != 'Administrator' &&
                   !user.replies.include?(reply)
    reply.update_attribute(:text, text)
    # reply.text = text
    reply.update_attribute(:last_edit_by, user)
  end

  def add_rating(user, stars)
    return false if stars < 1 || stars > 5
    # ind = @users_who_rated.index(user)
    rating = Rating.find_by(account_id: user.id, topic_id: id)
    if rating.nil?
      Rating.create('account' => user, 'topic' => self, 'rating' => stars)
    else
      rating.update_attribute(:rating, stars)
    end
  end
end
